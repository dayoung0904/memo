<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글쓰기</h1>
		
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요">
		<textarea id="content" class="form-control" rows="10" placeholder="내용을 입력하세요"></textarea>
		
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg, .jpeg, .png, .gif">
		</div>
		
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			<div>
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="saveBtn" class="btn btn-warning">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	// 목록 버튼 클릭 => 글 목록 화면 이동 a태그로하면 이동하는 주소가 보임, 버튼은 안보임
	$('#postListBtn').on('click', function(){
		//alert("클릭");
		location.href = "/post/post-list-view";
	});
	
	// 모두지우기 버튼 클릭
	$('#clearBtn').on('click',function(){
		$('#subject').val("");		
		$('#content').val("");		
	});
	
	// 글 저장 버튼
	$('#saveBtn').on('click',function(){
		let subject = $('#subject').val().trim();
		let content = $('#content').val();
		let fileName = $('#file').val(); // C:\fakepath\bird-8311912_1280.jpg
		
		// alert(file);
		
		// validation check
		if(!subject){
			alert("제목을 입력하세요.");
			return
		}
		if(!content){
			alert("내용을 입력하세요.");
			return
		}
		
		// 파일이 업로드 된 경우에만 확장자 체크
		if(fileName){
			// alert("파일이 있다.");
			// C:\fakepath\bird-8311912_1280.jpg
			// 확장자만 뽑은 후 소문자로 변경한다.
			let ext = fileName.split(".").pop().toLowerCase(); // pop() > 마지막 index의 값을 꺼내오는 함수  +  toLowerCase() > 소문자로 변경
			//alert(ext);
			
			if($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1){ // inArray(비교할 대상, 비교 배열)
				alert("이미지 파일만 업로드 할 수 있습니다.");
				$('#file').val(""); // 이미지가 아닌 머물러 있는 파일을 비운다.
				return;
			}
		}
		
		// AJAX
		// request param 구성
		// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다.
		let formData = new FormData();
		formData.append("subject", subject); // key는 form태그의 name 속성과 같고 request parameter명이 된다.
		formData.append("content", content);
		formData.append("file", $('#file')[0].files[0]); // 0번째 file중에서 0번째 파일을 가져온다.
		
		$.ajax({
			// request
			type:"post"
			, url:"/post/create"
			, data:formData
			, enctype:"multipart/form-data" // 파일업로드를 위한 필수 설정
			, processData:false // 파일업로드를 위한 필수 설정
			, contentType:false // 파일업로드를 위한 필수 설정
			
			// response
			, success:function(data){
				if(data.code == 200){
					alert("메모가 저장되었습니다.");
					location.href = "/post/post-list-view"
				} else{
					// 로직 실패
					alert(data.errorMessage);
				}
			}
			, error:function(request, status, error){
				alert("글을 저장하는데 실패했습니다.");
			}
		});
	});
});

</script>