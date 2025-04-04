package kr.co.greendae.service;

import kr.co.greendae.dto.user.TermsDTO;
import kr.co.greendae.entity.user.Terms;
import kr.co.greendae.repository.user.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;
    private final ModelMapper modelMapper;

    public TermsDTO findTerms() {

        Optional<Terms> optTerms = termsRepository.findById(1);

        if (optTerms.isPresent()) {
            TermsDTO termsDTO = modelMapper.map(optTerms.get(), TermsDTO.class);
            return termsDTO;
        }

        return null;
    }
}
