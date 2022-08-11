package org.yangyi.project;

import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.TimeZone;

@SpringBootApplication
public class ShiroMain {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));
        SpringApplication.run(ShiroMain.class, args);

        // 84坐标系构造GeodeticCalculator
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
        // 起点经纬度
        geodeticCalculator.setStartingGeographicPoint(120, 0);
        // 末点经纬度
        geodeticCalculator.setDestinationGeographicPoint(121,0);
        // 计算距离，单位：米
        double orthodromicDistance = geodeticCalculator.getOrthodromicDistance();
        System.err.println(orthodromicDistance);

        double temp = ShiroMain.getDistance(120, 0, 121, 0);
        System.err.println(temp);
        BigDecimal two = new BigDecimal(temp);
        two = two.multiply(new BigDecimal(1000));
        double three = two.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
        System.err.println(three);
    }

    @PostConstruct
    void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    /**
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double EARTH_RADIUS = 6378.137;
        // 纬度
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        // 经度
        double lng1 = Math.toRadians(longitude1);
        double lng2 = Math.toRadians(longitude2);
        // 纬度之差
        double a = lat1 - lat2;
        // 经度之差
        double b = lng1 - lng2;
        // 计算两点距离的公式
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        // 弧长乘地球半径, 返回单位: 千米
        s = s * EARTH_RADIUS;
        return s;
    }

}
