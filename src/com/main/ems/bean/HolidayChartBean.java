package main.ems.bean;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import main.ems.model.User;
import main.ems.service.HolidayChartService;
import main.ems.util.UserBeanInstance;

@ManagedBean(name = "holidayChartBean")
@ViewScoped
public class HolidayChartBean {

    @Autowired
    HolidayChartService holidayChartService;
    private LineChartModel lineModel;

    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        createLineModels();
    }

    private void createLineModels() {
        lineModel = initLinearModel();
        lineModel.setTitle("Holiday Availability");
        lineModel.setLegendPosition("ne");
        Axis yAxis = lineModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
        LineChartSeries series = new LineChartSeries();
        series.setLabel("Series 1");

        series.set(1, 2);
        series.set(2, 1);
        series.set(3, 3);
        series.set(4, 6);
        series.set(5, 8);

        model.addSeries(series);
        return model;
    }
}
