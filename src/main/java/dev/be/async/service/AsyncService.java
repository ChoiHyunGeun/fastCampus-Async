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
     * 스프링 빈 의존성이 정상적으로 주입된 객체를 사용해서, 의도한대로 비동기적으로 실행됨
     */
    public void asyncCall_1() {
        System.out.println("[asyncCall_1] :: " + Thread.currentThread().getName());
        emailService.sendMail();
        emailService.sendMailWithCustomThreadPool();
    }

    /**
     * 2. 인스턴스를 생성하여 그 인스턴스 안에 있는 메서드를 호출하는 방법
     * > 결과 모두 동일한 스레드가 처리함
     * 함수 내에서 직접 인스턴스를 생성했기 때문에 스프링이 관리하는 객체의 도움을 받을 수 없음
     * 그래서 EmailService 내부에 있는 함수에 @Async 어노테이션이 선언되었다고 해도 비동기적으로 실행되지 않는다.
     */
    public void asyncCall_2() {
        System.out.println("[asyncCall_2] :: " + Thread.currentThread().getName());
        EmailService emailService = new EmailService();
        emailService.sendMail();
        emailService.sendMailWithCustomThreadPool();
    }

    /**
     * 3. 클래스 내부에 있는 Async 메서드를 호출하는 방법
     * 스프링에서 AOP 프록시를 통한 비동기 실행이 작동하려면, 비동기 메소드가 프록시를 통해 호출되어야 한다.
     * 그러나 같은 클래스 내에서 메소드를 직접 호출하면 이 호출은 프록시를 거치지 않고 직접적으로 이뤄지기 때문에
     * AOP 인터셉터가 작동하지 않는다.
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
