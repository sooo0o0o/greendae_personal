package kr.co.greendae.service;

import kr.co.greendae.dto.event.EventDTO;
import kr.co.greendae.entity.event.Event;
import kr.co.greendae.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AcademicService {

    private final EventRepository eventRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public List<EventDTO> findAllEvent() {

        List<Event> events = eventRepository.findAll();
        List<EventDTO> list = new ArrayList<>();
        for (Event event : events) {
            EventDTO eventDTO = modelMapper.map(event, EventDTO.class);
            list.add(eventDTO);
        }
        return list;
    }
}
