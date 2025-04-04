document.querySelector("#course_list").addEventListener("click", function(event) {
    if (event.target && event.target.classList.contains('details')) {
        //const gradeData = event.target.dataset.grade;

        //alert('dfdfdf');
        const button = event.target;
        const tr = button.closest('tr');

        console.log(tr);
        const regLecNo = tr.querySelector('td:nth-child(1)').innerText;
        const lecName = tr.querySelector('td:nth-child(2)').innerText;
        const lecGrade = tr.querySelector('td:nth-child(3)').innerText;
        const lecProName = tr.querySelector('td:nth-child(4)').innerText;
        const lecCate = tr.querySelector('td:nth-child(5)').innerText;
        const regTotalScore = tr.querySelector('td:nth-child(6)').innerText;
        const regGradeScore = tr.querySelector('td:nth-child(7)').innerText;
        const regCredit = tr.querySelector('td:nth-child(8)').innerText;
        console.log(regLecNo);
        console.log(lecName);
        console.log(lecGrade);
        console.log(lecProName);
        console.log(lecCate);
        console.log(regTotalScore);
        console.log(regGradeScore);
        console.log(regCredit);



        // 모달 띄우기
        document.getElementById('details').style.display = "block";


    }
});


// 모달 닫기
    document.querySelectorAll('.close').forEach(closeBtn => {
    closeBtn.onclick = function() {
        document.getElementById('details').style.display = "none";
    }
});

    // 배경 클릭 시 모달 닫기
    window.onclick = function(event) {
    if (event.target == document.getElementById('details')) {
    document.getElementById('details').style.display = "none";
}
};

    // 학기 선택 변경 시 학기 목록 업데이트
    const semesterData = {
    "2025": ["1", "2"],
    "2024": ["1", "2"],
    "2023": ["1", "2"]
};

    function updateSemester(event) {
    let year = event.target.value;  // 선택된 년도
    let semesterSelect = document.getElementById("semester_quarter"); // 학기 선택
    semesterSelect.innerHTML = "";  // 기존 옵션 삭제

    // 새로운 학기 옵션 추가
    if (semesterData[year]) {
    semesterData[year].forEach(semester => {
    let option = document.createElement("option");
    option.value = semester;
    option.textContent = semester;
    semesterSelect.appendChild(option);
});
}
}

    // 페이지 로딩 시 학기 정보 업데이트
    window.onload = function() {
    const semesterYearSelect = document.getElementById("semester_year");
    if (semesterYearSelect) {
    semesterYearSelect.addEventListener("change", updateSemester);
    updateSemester({ target: semesterYearSelect });
}
};
