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