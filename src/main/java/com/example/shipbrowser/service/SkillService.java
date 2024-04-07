package com.example.shipbrowser.service;

import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public void remove(List<Skill> skillsToRemove) {
        skillRepository.deleteAll(skillsToRemove);
    }
}
