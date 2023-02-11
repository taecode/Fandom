<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="cpath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Default</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/x-icon" href="assets/img/favicon.ico">

    <link rel="stylesheet" href="/css/bootstrap.min.css">
        <link rel="stylesheet" href="/css/memberStyle.css">
        <link rel="stylesheet" href="/css/my.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>

<body>
    <main class="login-body slider-bg">

            <div class="login-form">
                <h2>Registration Here</h2>
                <div class="form-input">
                    <label for="name">Email Address</label>
                    <input type="email" name="userEmail" id="memEmail" placeholder="Email Address">
                         <span id="memEmailRight"></span>
                </div>
                <div class="form-input">
                    <label for="name">Password</label>
                    <input type="password" name="userPw" id="memPassword" placeholder="Password">
                       <span id="memPasswordRight"></span>
                </div>
                <div class="form-input">
                    <label for="name">Confirm Password</label>
                    <input type="password" name="userPw-confirm" id="memPasswordCheck" placeholder="Confirm Password">
                       <span id="memPasswordCheckRight"></span>
                </div>
                <div class="form-input pt-30">
                    <input type="button" name="submit" value="Registration" id="signUpBtn">
                </div>
            </div>
    </main>
    <script src="/js/bootstrap.min.js"></script>

<script>
let flag1=false;
let flag2=false;
let flag3=false;
const $memEmail=document.getElementById('memEmail');
const $memEmailRight=document.getElementById('memEmailRight');
const $memPassword=document.getElementById('memPassword');
const $memPasswordRight=document.getElementById('memPasswordRight');
const $memPasswordCheck=document.getElementById('memPasswordCheck');
const $memPasswordCheckRight=document.getElementById('memPasswordCheckRight');
const $signUpBtn=document.getElementById('signUpBtn');
$memEmail.onkeyup=function() {
        const emailRegex = /^[a-z0-9\.\-_]+@([a-z0-9\-]+\.)+[a-z]{2,6}$/;
        if (!$memEmail.value) {
            $memEmailRight.innerHTML ='아이디는 필수로 입력해야 합니다.';
              $memEmailRight.style.color='red';
        }else if (!emailRegex.test($memEmail.value)) {
              $memEmailRight.innerHTML ='이메일 형식에 맞춰서 입력해 주세요.';
              $memEmailRight.style.color='red';
        }else{
            $memEmailRight.innerHTML ='';
            fetch(`/member/check?memEmail=`+$memEmail.value)
            .then(res=>res.json())
            .then(flag=>{
                  if(flag){
                     $memEmailRight.innerHTML ='중복된 이메일 입니다.';
                      $memEmailRight.style.color='red';
                }else{
                      $memEmailRight.innerHTML ='가입 할 수 있는 이메일 입니다.';
                       $memEmailRight.style.color='white';
                      flag1=true;
                }
             } );
        }
}
$memPassword.onkeyup=function() {
    const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,20}$/;
    if (!$memPassword.value) {
                $memPasswordRight.innerHTML ='비밀번호는 필수값입니다!';
                $memPasswordRight.style.color='red';
            } else if (!pwRegex.test($memPassword.value)) {
                $memPasswordRight.innerHTML = '8글자 이상의 영문,숫자,특수문자를 포함해주세요!';
                 $memPasswordRight.style.color='red';
            } else {
                $memPasswordRight.innerHTML = '사용 가능한 비밀번호입니다.';
                $memPasswordRight.style.color='white';
                flag2=true;
            }
};
$memPasswordCheck.onkeyup=function() {
               if (!$memPasswordCheck.value) { //패스워드 안적은 상황
                    $memPasswordCheckRight.innerHTML ='비밀번호는 필수값입니다!';
                     $memPasswordCheckRight.style.color='red';
                   } else if ($memPasswordCheck.value!==$memPassword.value) {
                       $memPasswordCheckRight.innerHTML = '1차 비밀번호와 일치하게 작성해주세요!';
                            $memPasswordCheckRight.style.color='red';
                   } else {
                        $memPasswordCheckRight.style.color='white';
                        $memPasswordCheckRight.innerHTML = '비밀번호가 일치합니다.';
                        flag3=true;
                   }
};
$signUpBtn.onclick=function(){
    if(flag1 &&flag2&&flag3){
           const userValue={
                    memEmail:$memEmail.value,
                    memPassword:$memPassword.value
            }
              fetch('/member/signup',{
                            method:'POST',
                            headers:{
                                'content-type':'application/json'
                            },
                            body:JSON.stringify(userValue)
                        })
                        .then(res=>{
                            if(res.status===200){
                                alert('회원가입을축하합니다.');
                                window.location.href = '/member/signin';
                            }else{
                                alert('회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.');
                            }
                        });
   }
}
</script>
</body>
</html>