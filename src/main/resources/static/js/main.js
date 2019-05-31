$(document).ready(function () {

    $("#search-form").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();

        fire_ajax_submit();
    });

});

function fire_ajax_submit() {
    var data = $("#search-form").serializeArray();
    
    var formArray = {};
    
    $.map(data, function(n, i){
        formArray[n['name']] = n['value']; // key - value
    });

    var json =JSON.stringify(formArray);
    console.log(json);
    $.ajax({
        type: 'post',
        contentType: 'application/json; charset=UTF-8',
        url: '/users',
        data: json,
        dataType: 'text',
        success: function (formArray) {
        	console.log("SUCCESS : ", formArray);
        	window.location.href = "/users"; // get 방식
        	
        },
        error: function (e) {
        	console.log("ERROR : ", e);        	
        }
    });

}

function button_event() {
	if (confirm("정말 삭제하시겠습니까??") == true) { //확인
		document.deleteform.submit();
	} else { //취소
		return false;
	}
}


