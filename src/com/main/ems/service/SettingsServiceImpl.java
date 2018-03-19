package main.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.ems.dao.SettingsDao;
import main.ems.model.Settings;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsDao settingsDao;

    @Override
    @Transactional
    public Settings getSetting(int id) {
        return settingsDao.getSetting(id);
    }

    @Override
    @Transactional
    public void saveSalarySettings(List<Settings> settings) {
        settingsDao.saveSalarySettings(settings);
    }
}
