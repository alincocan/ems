package main.ems.service;

import java.util.List;

import main.ems.model.Settings;

public interface SettingsService {
    public Settings getSetting(int id);

    public void saveSalarySettings(List<Settings> settings);
}
