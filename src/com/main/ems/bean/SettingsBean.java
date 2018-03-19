package main.ems.bean;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.Settings;
import main.ems.service.SettingsService;
import main.ems.util.AddMessage;

@ManagedBean(name = "settingsBean")
@ViewScoped
public class SettingsBean {

    Boolean bilunar;
    Settings bilunarSet;
    Settings firstDay;
    Settings secondDay;
    Settings day;
    Properties props;

    @Autowired
    SettingsService settingsService;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);

        try {
            bilunarSet = settingsService.getSetting(Settings.BILUNAR);
            if (bilunarSet.getValue().equals("true")) {
                bilunar = true;
            } else {
                bilunar = false;
            }
            firstDay = settingsService.getSetting(Settings.FIRST_DAY_OF_PAYMENT);
            secondDay = settingsService.getSetting(Settings.SECOND_DAY_OF_PAYMENT);
            day = settingsService.getSetting(Settings.DAY_OF_PAYMENT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean disableSaveButton() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int lastDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDayOfMonth != lastDayOfMonth) {
            return true;
        } else {
            return false;
        }
    }

    public void saveSalaryChanges() {
        List<Settings> settings = new ArrayList<Settings>();
        bilunarSet.setValue(new String(bilunar.toString()));
        settings.add(bilunarSet);
        settings.add(firstDay);
        settings.add(secondDay);
        settings.add(day);

        try {
            settingsService.saveSalarySettings(settings);
            new AddMessage().addInfo("Succes");
        } catch (Exception ex) {
            ex.printStackTrace();
            new AddMessage().addError("Error");
        }
    }

    public boolean isBilunar() {
        return bilunar;
    }

    public void setBilunar(boolean bilunar) {
        this.bilunar = bilunar;
    }

    public Settings getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(Settings firstDay) {
        this.firstDay = firstDay;
    }

    public Settings getSecondDay() {
        return secondDay;
    }

    public void setSecondDay(Settings secondDay) {
        this.secondDay = secondDay;
    }

    public Settings getDay() {
        return day;
    }

    public void setDay(Settings day) {
        this.day = day;
    }

    public Settings getBilunarSet() {
        return bilunarSet;
    }

    public void setBilunarSet(Settings bilunarSet) {
        this.bilunarSet = bilunarSet;
    }
}
