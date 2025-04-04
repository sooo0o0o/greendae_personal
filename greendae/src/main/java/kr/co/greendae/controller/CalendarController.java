package kr.co.greendae.controller;

import kr.co.greendae.dto.event.EventDTO;
import kr.co.greendae.repository.event.EventRepository;
import kr.co.greendae.service.AcademicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final AcademicService academicService;

    // 🔹 학사 일정 데이터를 JSON으로 반환
    @GetMapping("/schedule")
    public List<Map<String, Object>> getScheduleEvents() {
        List<Map<String, Object>> eventList = new ArrayList<>();

        List<EventDTO> list = academicService.findAllEvent();

        for (EventDTO eventDTO : list) {
            eventList.add(createEvent(eventDTO.getTitle(), eventDTO.getDate()));
        }

        return eventList; // Spring Boot가 자동으로 JSON 변환
    }

    private Map<String, Object> createEvent(String title, String date) {
        Map<String, Object> event = new HashMap<>();
        event.put("title", title);
        event.put("start", date);
        return event;
    }
}
