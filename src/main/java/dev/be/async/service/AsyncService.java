package dev.be.async.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 케이스에 따라 비동기로 동작하는 지 확인하기 위함
 */
@RequiredArgsConstructor
@Service
public class AsyncService {
    private final EmailService emailService;

    /**
     * 1. 빈을 주입받아서 주입받은 빈 안에 있는 메서드를 호출하는 방법
     * > 각각 모두 다른 스레드가 처리함. 비동기하게 동작함
     * 스프링 컨테이너에 등록되어 있는 빈을 사용해야 한다는게 포인트
     */
    public void asyncCall_1() {
        System.out.println("[asyncCall_1] :: " + Thread.currentThread().getName());
        emailService.sendMail();
        emailService.sendMailWithCustomThreadPool();
    }

    /**
     * 2. 인스턴스를 생성하여 그 인스턴스 안에 있는 메서드를 호출하는 방법
     * > 결과 모두 동일한 스레드가 처리함
     * new 키워드로 인스턴스를 생성하면 스프링 컨테이너의 어떠한 도움도 받지 못함. 그래서 비동기로 처리되지 않는다.
     */
    public void asyncCall_2() {
        System.out.println("[asyncCall_2] :: " + Thread.currentThread().getName());
        EmailService emailService = new EmailService();
        emailService.sendMail();
        emailService.sendMailWithCustomThreadPool();
    }

    /**
     * 3. 클래스 내부에 있는 Async 메서드를 호출하는 방법
     */
    public void asyncCall_3() {
        System.out.println("[asyncCall_3] :: " + Thread.currentThread().getName());
        sendMail();
    }

    @Async("defaultTaskExecutor")
    public void sendMail() {
        System.out.println("[sendMail] :: " + Thread.currentThread().getName());
    }
}
