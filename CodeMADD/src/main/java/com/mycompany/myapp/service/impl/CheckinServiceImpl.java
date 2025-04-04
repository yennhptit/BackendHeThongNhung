package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Checkin;
import com.mycompany.myapp.repository.CheckinRepository;
import com.mycompany.myapp.service.CheckinService;
import com.mycompany.myapp.service.dto.CheckinDTO;
import com.mycompany.myapp.service.mapper.CheckinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Checkin}.
 */
@Service
@Transactional
public class CheckinServiceImpl implements CheckinService {

    private final Logger log = LoggerFactory.getLogger(CheckinServiceImpl.class);

    private final CheckinRepository checkinRepository;

    private final CheckinMapper checkinMapper;

    public CheckinServiceImpl(CheckinRepository checkinRepository, CheckinMapper checkinMapper) {
        this.checkinRepository = checkinRepository;
        this.checkinMapper = checkinMapper;
    }

    @Override
    public CheckinDTO save(CheckinDTO checkinDTO) {
        log.debug("Request to save Checkin : {}", checkinDTO);
        Checkin checkin = checkinMapper.toEntity(checkinDTO);
        checkin = checkinRepository.save(checkin);
        return checkinMapper.toDto(checkin);
    }

    @Override
    public CheckinDTO update(CheckinDTO checkinDTO) {
        log.debug("Request to update Checkin : {}", checkinDTO);
        Checkin checkin = checkinMapper.toEntity(checkinDTO);
        checkin = checkinRepository.save(checkin);
        return checkinMapper.toDto(checkin);
    }

    @Override
    public Optional<CheckinDTO> partialUpdate(CheckinDTO checkinDTO) {
        log.debug("Request to partially update Checkin : {}", checkinDTO);

        return checkinRepository
            .findById(checkinDTO.getId())
            .map(existingCheckin -> {
                checkinMapper.partialUpdate(existingCheckin, checkinDTO);

                return existingCheckin;
            })
            .map(checkinRepository::save)
            .map(checkinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CheckinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Checkins");
        return checkinRepository.findAll(pageable).map(checkinMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckinDTO> findOne(Long id) {
        log.debug("Request to get Checkin : {}", id);
        return checkinRepository.findById(id).map(checkinMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Checkin : {}", id);
        checkinRepository.deleteById(id);
    }
}
