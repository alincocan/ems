package main.ems.dao;

import java.util.List;

import main.ems.model.Settings;

public interface SettingsDao {
    public Settings getSetting(int id);

    public void saveSalarySettings(List<Settings> settings);
}
