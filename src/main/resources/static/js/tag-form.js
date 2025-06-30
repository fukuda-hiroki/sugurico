//  最初に、処理で使うhtml要素を取得しておく
const tagContainer = document.getElementById('tag-container');
const maxTags = 10;
let addInputTimer;  // 入力欄追加を出バウンスするためのタイマーID
let suggestTimer;   // サジェストAPI呼び出しを出バウンスためのタイマーID


tagContainer.addEventListener('input', handleTagInput);
tagContainer.addEventListener('keydown', handleKeyDown);
tagContainer.addEventListener('focusout', handleFocusOut)


//  テキストが入力された時、または削除された時の処理
function handleTagInput(event) {
    const targetInput = event.target;

    // 入力欄の増減処理(デバウンス)
    clearTimeout(addInputTimer);
    addInputTimer = setTimeout(() => {
        if (targetInput.value.trim() === '' 
        && !isLastInput(targetInput)) {
           targetInput.parentElement.remove();
           updateTagInputNames(); 
        } else if (isLastInput(targetInput) && 
        targetInput.value.trim() !== '' &&
        tagContainer.children.length < maxTags) {
            addTagInput();
        }5
    }, 150);    // 150ミリ秒待ってから実行

    // --- サジェスト機能の呼び出し（デバウンス） ---
    clearTimeout(suggestTimer);
    suggestTimer = setTimeout(() => {
        fetchSuggestions(targetInput);
    }, 300);// 300ミリ秒待ってからAPIをたたく
}

function handleKeyDown(event) {
    const targetInput = event.target;

    if(event.key === 'Enter') {
        event.preventDefault(); //  フォーム送信を防止
        const nextInput = findNextInput(targetInput);
        if (nextInput) {
            nextInput.focus();
        } else if (tagContainer.children.length < maxTags) {
            addTagInput();
        }
    }

    if (event.key === 'Backspace' && 
        targetInput.value === '' &&
        tagContainer.children.length > 1
    ) {
        const prevInput = findPrevInput(targetInput);
        if (prevInput) {
            targetInput.parentElement.remove();
            updateTagInputNames();
            prevInput.focus();
        }
    }
}

function handleFocusOut(event) {
    setTimeout(() => {
        clearSuggestions(event.target);
    }, 200);
}

//  新しいタグ入力欄を追加する関数
function addTagInput() {
    if (tagContainer.children.length >= maxTags) return;
    const newIndex = tagContainer.children.length;

    const wrapper = document.createElement('div');
    wrapper.className = 'tag-input-wrapper';

    const newInput = document.createElement('input');
    newInput.type = 'text';
    newInput.name = `tags[${newIndex}]`;
    newInput.placeholder = 'タグを入力';

    wrapper.appendChild(newInput);
    tagContainer.appendChild(wrapper);
    //newInput.focus();
}

async function fetchSuggestions(inputElement) {
    const query = inputElement.value.trim();
    // 入力値が1文字未満なら、サジェストをクリアして終了
    if (query.length < 1) {
        clearSuggestions(inputElement);
        return;
    }

    //  現在入力されているすべてのタグを取得する
    const allInputs = Array.from(tagContainer.querySelectorAll('input'));
    const excludeTags = allInputs.filter(input => input !== inputElement && input.value.trim() !== '').map(input => input.value.trim());

    //  除外リストをURLのクエリパラメータに追加する
    const excludeParams = excludeTags.map(tag => `exclude=${encodeURIComponent(tag)}`).join('&');

    try{
        // バックエンドのAPIにリクエストを送信

        // ★ 既存の /api/tags/suggest を呼び出します
        const response = await fetch(`/api/tags/suggest?query=${encodeURIComponent(query)}&${excludeParams}`);
        if (!response.ok) throw new Error('Network response was not ok');
        // レスポンスをJSONとして解析
        const suggestions = await response.json();
        
        // 取得した候補でサジェストリストを表示
        showSuggestions(inputElement,suggestions);
    } catch (error) {
        console.error('サジェストの取得に失敗しました:', error);
        clearSuggestions(inputElement);
    }
}

function showSuggestions(inputElement, suggestions) {
    clearSuggestions(inputElement);
    // 既存のリストをクリア

    if(suggestions.length === 0 ) return;

    const suggestionsList = document.createElement('ul');
    suggestionsList.className = 'suggestions-list'; // CSSでスタイルを当てるためのクラス

    suggestions.forEach(suggestionText => {
        const listItem = document.createElement('li');
        listItem.textContent = suggestionText;
        // ★'mousedown'イベント: 'click'だと入力欄のfocusoutが先に発火してしまうため
        listItem.addEventListener('mousedown', () =>{
            inputElement.value = suggestionText;    // 候補をクリックしたら入力欄に値をセット
            clearSuggestions(inputElement); // リストを消す
        });
        suggestionsList.appendChild(listItem);
        // input要素の親要素(div.tag-input-wrapper)にサジェストリストを追加
    });
    inputElement.parentElement.appendChild(suggestionsList);
}

function clearSuggestions(inputElement) {
    const existingList = inputElement.parentElement.querySelector('.suggestions-list');
    if(existingList) {
        existingList.remove()
    }
}

// 入力欄が削除された後にname属性のインデックスを振りなおす関数
function updateTagInputNames() {
    const wrappers = tagContainer.children;
    for (let i = 0; i < wrappers.length; i++) {
        const input = wrappers[i].querySelector('input');
        input.name = `tags[${i}]`;
    }
}

//  指定された入力欄が最後のものかどうかを判定する関数
function isLastInput(input) {
    const wrappers = tagContainer.children;
    //  見えない要素も考慮するため、nullチェックを追加
    if (!input || !input.parentElement) {
        return false;
    }
    return input.parentElement === wrappers[wrappers.length - 1];
}

function findNextInput(currentInput) {
    const nextWrapper = currentInput.parentElement.nextElementSibling;
    return nextWrapper ? nextWrapper.querySelector('input') : null;
}

function findPrevInput(currentInput) {
    const prevWrapper = currentInput.parentElement.previousElementSibling;
    return prevWrapper ? prevWrapper.querySelector('input') : null;
}

//  最初の空の入力欄を見つける関数
function findFirstEmptyInput() {
    const inputs = tagContainer.querySelectorAll('input');
    for (const input of inputs) {
        if (input.value.trim() === '') {
            return input;
        }
    }
    return null;
}