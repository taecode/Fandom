package com.example.NewJeans.controller;

import com.example.NewJeans.dto.request.LoginRequestDTO;
import com.example.NewJeans.dto.request.SignUpRequestDTO;
import com.example.NewJeans.dto.response.LoginResponseDTO;
import com.example.NewJeans.dto.response.SignUpResponseDTO;
import com.example.NewJeans.exception.DuplicatedEmailException;
import com.example.NewJeans.exception.NoRegisteredArgumentsException;
import com.example.NewJeans.service.MemberServcie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberServcie memberServcie;

    @GetMapping("/signin")
    public String signIn(){
        return "member/memberSignIn";
    }
    @GetMapping("/signup")
    public String signUp(){
        return "member/memberSignUp";
    }

    @PostMapping("/signin")
    public @ResponseBody  ResponseEntity<?> signIn(@Validated @RequestBody LoginRequestDTO loginRequestDTO){

        try{
            LoginResponseDTO loginResponseDTO =memberServcie.getByCredentials(loginRequestDTO.getMemEmail(), loginRequestDTO.getMemPassword());
            return ResponseEntity
                    .ok()
                    .body(loginResponseDTO);
        }catch (RuntimeException e){
            return ResponseEntity
                    .badRequest()
                    .build();

        }

    }



    @GetMapping("/check")
    public @ResponseBody ResponseEntity<?> checkEmail(String memEmail){

        if (memEmail ==null || memEmail.trim().equals("")){
            return ResponseEntity.badRequest().body("이메일을 전달해 주세요");
        }

        boolean flag = memberServcie.isDuplicate(memEmail);
        log.info("{} 중복 여부 ?? = {}",memEmail,flag);
        return ResponseEntity.ok().body(flag);
    }
    @PostMapping("/signup")
    public  @ResponseBody ResponseEntity<?> signUp(@Validated @RequestBody SignUpRequestDTO signUpRequestDTO, BindingResult result){
        log.info("/api/auth/signup POST!  - {}" ,signUpRequestDTO);

        if (result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.toString());
        }

        try {
            SignUpResponseDTO signUpResponseDTO = memberServcie.create(signUpRequestDTO);
            return ResponseEntity
                    .ok()
                    .body(signUpResponseDTO);

        }catch (NoRegisteredArgumentsException e){
            log.warn("필수 가입 정보를 다시 확인하세요.");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }catch (DuplicatedEmailException e){
            log.warn("중복되었습니다. 다른 이메일을 작성해 주세요");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }




}