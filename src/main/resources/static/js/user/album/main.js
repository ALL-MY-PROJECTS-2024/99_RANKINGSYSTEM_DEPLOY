//// 필터 모달창 여닫기
//const open = document.querySelector(".filter-icon");
//const close = document.querySelector(".modal_closeBtn");
//const modal = document.querySelector(".modal");
//
//open.addEventListener('click',function(){
//  modal.classList.toggle('hidden');
//})
//close.addEventListener("click", function(){
//  modal.classList.add("hidden");
//});

// 백그라운드 클릭시 창닫기 추가


//// 필터 카테고리 클릭 이벤트
//function change_btn(e) {
//    const btns = document.querySelectorAll(".filter_button");
//    btns.forEach(function (btn, i) {
//         if (e.currentTarget == btn) {
//        btn.classList.add("active");
//      } else {
//        btn.classList.remove("active");
//      }
//    });
//    console.log(e.currentTarget);
//  }
//
//// 검색 키보드 이벤트(엔터) 추가하기




//음악 / 이미지 버튼
const btnImg = document.querySelector('.my-btn.btn-img');
const btnMusic = document.querySelector('.my-btn.btn-music');

const imageBlock = document.querySelector('.image-block.items');
const musicBlock = document.querySelector('.music-block.items');

btnImg.addEventListener('click',function(){

    btnImg.classList.add('active')
    btnMusic.classList.remove('active');

    imageBlock.classList.remove('hidden');
    musicBlock.classList.add('hidden');

})
btnMusic.addEventListener('click',function(){
    btnMusic.classList.add('active')
    btnImg.classList.remove('active');

    musicBlock.classList.remove('hidden');
    imageBlock.classList.add('hidden');

})



