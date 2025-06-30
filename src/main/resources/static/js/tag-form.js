//  最初に、処理で使うhtml要素を取得しておく
const tagContainer = document.getElementById('tag-container');
const maxTags = 10;
let isAdding = false;

tagContainer.addEventListener('input', handleTagInput);

//  テキストが入力された時、または削除された時の処理
function handleTagInput(event) {
    if (isAdding) {
        return;
    }
    const targetInput = event.target;

    fetchSuggestions(targetInput);

    //  もし入力欄が空になりかつそれが最後の入力欄でない場合
    if (targetInput.value.trim() === '' && !isLastInput(targetInput)) {
        targetInput.parentElement.remove();
        updateTagInputNames();
        const firstEmptyInput = findFirstEmptyInput();
        if(firstEmptyInput){
            firstEmptyInput.focus();
        }
        return;
    }
    // もし入力欄が最後のもので、かつまだ最大数に達していない場合
    if(isLastInput(targetInput) && tagContainer.children.length < maxTags){
        addTagInput();
    }
}

async function fetchSuggestions(inputElement) {
    const query = inputElement.value.trim();
// 入力値が1文字未満なら、サジェストをクリアして終了
    if (query.length < 1) {
        clearSuggestions(inputElement);
        return;
    }

    try{// バックエンドのAPIにリクエストを送信
        // ★ 既存の /api/tags/suggest を呼び出します
        const response = await fetch(`/api/tags/suggest?query=${encodeURIComponent(query)}`);
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
    clearSuggestions(inputElement);// 既存のリストをクリア

    if(suggestions.length === 0 ) return;

    const suggestionsList = document.createElement('ul');
    suggestionsList.className = 'suggestions-list';// CSSでスタイルを当てるためのクラス

    suggestions.forEach(suggestionText => {
        const listItem = document.createElement('li');
        listItem.textContent = suggestionText;// ★'mousedown'イベント: 'click'だと入力欄のfocusoutが先に発火してしまうため
        listItem.addEventListener('mousedown', () =>{
            inputElement.value = suggestionText;// 候補をクリックしたら入力欄に値をセット
            clearSuggestions(inputElement);// リストを消す
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

//  新しいタグ入力欄を追加する関数
function addTagInput() {
    isAdding = true;
    const newIndex = tagContainer.children.length;
    const wrapper = document.createElement('div');
    wrapper.className = 'tag-input-wrapper';
    const newInput = document.createElement('input');
    newInput.type = 'text';
    newInput.name = `tags[${newIndex}]`;
    newInput.placeholder = 'タグを入力'
    wrapper.appendChild(newInput);
    tagContainer.appendChild(wrapper);
    
    newInput.focus();
    
    setTimeout(() => {
       isAdding = false; 
    }, 0);
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

// 入力欄が削除された後にname属性のインデックスを振りなおす関数
function updateTagInputNames() {
    const wrappers = tagContainer.children;
    for (let i = 0; i < wrappers.length; i++) {
        const input = wrappers[i].querySelector('input');
        input.name = `tags[${i}]`;
    }
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