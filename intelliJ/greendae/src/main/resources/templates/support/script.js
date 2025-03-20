// 모달 관련 스크립트
document.getElementById('btn_details').onclick = function() {
    document.getElementById('details').style.display = "block";
}

document.getElementsByClassName('close')[0].onclick = function() {
    document.getElementById('details').style.display = "none";
}

window.onclick = function(event) {
    if (event.target == document.getElementById('details')) {
        document.getElementById('details').style.display = "none";
    }
}

// 학기 선택 변경 시 학기 목록 업데이트
const semesterData = {
    "2025": ["1", "2"],
    "2024": ["1", "2"],
    "2023": ["1", "2"]
};

function updateSemester() {
    let year = document.getElementById("semester_year").value;  // 선택 년도
    let semesterSelect = document.getElementById("semester_quarter"); // 학기
    semesterSelect.innerHTML = "";  // 기존 옵션 삭제

    // 새로운 학기 옵션
    semesterData[year].forEach(semester => {
        let option = document.createElement("option");
        option.value = semester;
        option.textContent = semester;
        semesterSelect.appendChild(option);
    });
}

// 페이지 로딩 시 학기 정보 업데이트
window.onload = updateSemester;