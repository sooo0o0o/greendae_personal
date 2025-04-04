package kr.co.greendae.service;


import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import kr.co.greendae.dto.user.UserDTO;
import kr.co.greendae.entity.user.User;
import kr.co.greendae.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private final HttpServletRequest request;

    public void register(UserDTO userDTO) {
        // 비밀번호 암호화
        String encodedPass = passwordEncoder.encode(userDTO.getPass());
        userDTO.setPass(encodedPass);

        // 엔티티 변환
        User user = modelMapper.map(userDTO, User.class);

        // 저장
        userRepository.save(user);
    }
    public long checkUser(String type, String value){

        long count = 0;

        if(type.equals("uid")){
            count = userRepository.countByUid(value);
        } else if(type.equals("hp")){
            count = userRepository.countByHp(value);
        }else if(type.equals("email")){
            count = userRepository.countByEmail(value);

            if(count == 0){
                String code = sendEmailCode(value);

                // 인증코드 비교를 하기 위해서 세션 저장
                HttpSession session = request.getSession();
                session.setAttribute("authCode", code);
            }

        }
        return count;
    }

    @Value("${spring.mail.username}")
    private String sender;
    public String sendEmailCode(String receiver){

        // MimeMessage 생성
        MimeMessage message = mailSender.createMimeMessage();

        // 읹증코드 생성
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        log.info("code: " + code);

        String subject = "greendae 인증코드 안내";
        String content = "<h1>greendae 인증코드는 " + code + " 입니다.</h1>";

        try {

            message.setFrom(new InternetAddress(sender, "보내는사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO,new  InternetAddress(receiver));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");

            mailSender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return String.valueOf(code);

    }

    public String sendFindIdEmailCode(String receiver, String name) {
        // 이메일로 사용자 정보 조회 (예: 이메일로 User 엔티티를 조회)
        User user = userRepository.findByEmail(receiver);

        // 사용자 이름이 없으면 예외 처리
        if (user == null) {
            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        if (!user.getName().equals(name)) {
            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        // 인증코드 생성
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);

        String subject = "greendae 아이디 찾기 인증코드 안내";
        // 이메일 본문에 사용자 이름 포함
        String content = "<h1>" + name + "님, greendae 아이디 찾기 인증코드는 " + code + " 입니다.</h1>";

        // MimeMessage 생성
        MimeMessage message = mailSender.createMimeMessage();

        try {

            message.setFrom(new InternetAddress(sender, "보내는사람", "UTF-8"));
            message.addRecipient(Message.RecipientType.TO,new  InternetAddress(receiver));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");

            mailSender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return String.valueOf(code);
    }

    public UserDTO findUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }
    private String generateAuthCode() {
        // 6자리 인증 코드 생성 (100000 ~ 999999 범위)
        int code = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(code);  // 생성된 인증 코드를 문자열로 반환
    }

    public String sendPasswordResetEmailCode(String email, String uid) {
        // 인증 코드 생성
        String authCode = generateAuthCode();

        // 이메일 전송 시 로그 추가
        log.info("이메일 전송 시작 - 이메일: {}, 인증 코드: {}", email, authCode);

        // 이메일 전송
        boolean emailSent = sendResetPasswordEmail(email, authCode);

        if (emailSent) {
            log.info("이메일 전송 성공 - 이메일: {}", email);
            return authCode;
        } else {
            log.error("이메일 전송 실패 - 이메일: {}", email);
            throw new RuntimeException("이메일 전송에 실패했습니다.");
        }
    }

    private boolean sendResetPasswordEmail(String receiver, String authCode) {
        // MimeMessage 생성
        MimeMessage message = mailSender.createMimeMessage();

        // 이메일 제목과 내용 설정
        String subject = "greendae 비밀번호 재설정 인증코드 안내";
        String content = "<h1>greendae 비밀번호 재설정 인증코드는 " + authCode + " 입니다.</h1>";

        try {
            // 발신자 설정
            message.setFrom(new InternetAddress(sender, "보내는사람", "UTF-8"));
            // 수신자 설정
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            // 제목 설정
            message.setSubject(subject);
            // 내용 설정
            message.setContent(content, "text/html;charset=UTF-8");

            // 이메일 전송
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("이메일 전송 실패: " + e.getMessage());
            return false;
        }
    }

    public ResponseEntity<Map<String, Object>> updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "사용자를 찾을 수 없습니다."));
        }

        // 비밀번호 변경
        user.setPass(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "message", "비밀번호가 성공적으로 변경되었습니다."));
    }

}