package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.Skill;
import com.example.shipbrowser.dao.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public void remove(List<Skill> skillsToRemove) {
        skillRepository.deleteAll(skillsToRemove);
    }
}
