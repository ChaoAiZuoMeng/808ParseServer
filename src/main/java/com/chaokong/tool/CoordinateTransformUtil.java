 package com.chaokong.tool;

 import java.math.BigDecimal;





 public class CoordinateTransformUtil
 {
   static int B_DIV_SCALE = 15;
   static BigDecimal x_pi = (new BigDecimal("3.14159265358979324")).multiply(new BigDecimal("3000")).divide(new BigDecimal("180"), B_DIV_SCALE, 4);

   static BigDecimal pi = new BigDecimal("3.1415926535897932384626");

   static BigDecimal a = new BigDecimal("6378245");

   static BigDecimal ee = new BigDecimal("0.00669342162296594323");
   static BigDecimal b_1 = new BigDecimal("-1");
   static BigDecimal b_2 = new BigDecimal("10");
   static BigDecimal b_3 = new BigDecimal("2");
   static BigDecimal b_4 = new BigDecimal("105");
   static BigDecimal b_5 = new BigDecimal("3");
   static BigDecimal b_6 = new BigDecimal("35");
   static BigDecimal b_7 = new BigDecimal("160");
   static BigDecimal b_8 = new BigDecimal("1");
   static BigDecimal b_9 = new BigDecimal("150");
   static BigDecimal b_10 = new BigDecimal("180");

   static BigDecimal b_11 = new BigDecimal("0.00002");
   static BigDecimal b_12 = new BigDecimal("0.000003");
   static BigDecimal b_13 = new BigDecimal("0.0065");
   static BigDecimal b_14 = new BigDecimal("0.006");








   public static double[] wgs84tobd09(double lng, double lat) {
     double[] gcj = wgs84togcj02(lng, lat);
     double[] bd09 = gcj02tobd09(gcj[0], gcj[1]);
     return bd09;
   }









   public static double[] gcj02tobd09(double lng, double lat) {
     BigDecimal b_lng = new BigDecimal(lng);
     BigDecimal b_lat = new BigDecimal(lat);
     BigDecimal z = (new BigDecimal(Math.sqrt(b_lng.multiply(b_lng).add(b_lat.multiply(b_lat)).doubleValue()))).add(b_11.multiply(new BigDecimal(Math.sin(b_lat.multiply(x_pi).doubleValue()))));
     BigDecimal theta = (new BigDecimal(Math.atan2(b_lat.doubleValue(), b_lng.doubleValue()))).add(b_12.multiply(new BigDecimal(Math.cos(b_lng.multiply(x_pi).doubleValue()))));
     BigDecimal bd_lng = z.multiply(new BigDecimal(Math.cos(theta.doubleValue()))).add(b_13);

     BigDecimal bd_lat = z.multiply(new BigDecimal(Math.sin(theta.doubleValue()))).add(b_14);
     return new double[] { bd_lng.doubleValue(), bd_lat.doubleValue() };
   }








   public static double[] wgs84togcj02(double lng, double lat) {
     BigDecimal b_lng = new BigDecimal(lng + "");
     BigDecimal b_lat = new BigDecimal(lat + "");
     if (out_of_china(b_lng, b_lat)) {
       return new double[] { b_lng.doubleValue(), b_lat.doubleValue() };
     }
     BigDecimal dlat = transformlat(b_lng.subtract(b_4), b_lat.subtract(b_6));
     BigDecimal dlng = transformlng(b_lng.subtract(b_4), b_lat.subtract(b_6));
     BigDecimal radlat = b_lat.divide(b_10, B_DIV_SCALE, 4).multiply(pi);
     BigDecimal magic = new BigDecimal(Math.sin(radlat.doubleValue()));
     magic = b_8.subtract(ee.multiply(magic).multiply(magic));
     BigDecimal sqrtmagic = new BigDecimal(Math.sqrt(magic.doubleValue()));
     dlat = dlat.multiply(b_10).divide(a.multiply(b_8.subtract(ee)).divide(magic.multiply(sqrtmagic), B_DIV_SCALE, 4).multiply(pi), B_DIV_SCALE, 4);
     dlng = dlng.multiply(b_10).divide(a.divide(sqrtmagic, B_DIV_SCALE, 4).multiply((new BigDecimal(Math.cos(radlat.doubleValue()))).multiply(pi)), B_DIV_SCALE, 4);
     BigDecimal mglat = b_lat.add(dlat);
     BigDecimal mglng = b_lng.add(dlng);
     return new double[] { mglng.doubleValue(), mglat.doubleValue() };
   }













   public static BigDecimal transformlat(BigDecimal lng, BigDecimal lat) {
     BigDecimal bet = b_1.multiply(b_2).multiply(b_2).add(b_3.multiply(lng)).add(b_5.multiply(lat)).add(b_3.divide(b_2, B_DIV_SCALE, 4).multiply(lat).multiply(lat)).add(b_8.divide(b_2, B_DIV_SCALE, 4).multiply(lng).multiply(lat)).add(b_3.divide(b_2, B_DIV_SCALE, 4).multiply(new BigDecimal(Math.sqrt(Math.abs(lng.doubleValue())))));
     bet = bet.add(b_3.multiply(b_2).multiply(new BigDecimal(Math.sin(b_3.multiply(b_5).multiply(lng).multiply(pi).doubleValue())))
         .add(b_3.multiply(b_2).multiply(new BigDecimal(Math.sin(b_3.multiply(lng).multiply(pi).doubleValue())))).multiply(b_3).divide(b_5, B_DIV_SCALE, 4));
     bet = bet.add(b_3.multiply(b_2).multiply(new BigDecimal(Math.sin(lat.multiply(pi).doubleValue())))
         .add(b_3.multiply(b_3).multiply(b_2).multiply(new BigDecimal(Math.sin(lat.divide(b_5, B_DIV_SCALE, 4).multiply(pi).doubleValue())))).multiply(b_3).divide(b_5, B_DIV_SCALE, 4));
     bet = bet.add(b_7.multiply(new BigDecimal(Math.sin(lat.divide(b_5.multiply(b_3).multiply(b_3), B_DIV_SCALE, 4).multiply(pi).doubleValue())))
         .add(b_7.multiply(b_3).multiply(new BigDecimal(Math.sin(lat.multiply(pi).divide(b_5.multiply(b_2), B_DIV_SCALE, 4).doubleValue())))).multiply(b_3).divide(b_5, B_DIV_SCALE, 4));
     return bet;
   }









   public static BigDecimal transformlng(BigDecimal lng, BigDecimal lat) {
     BigDecimal bet = b_2.multiply(b_2).multiply(b_5).add(lng).add(b_3.multiply(lat)).add(b_8.divide(b_2, B_DIV_SCALE, 4).multiply(lng).multiply(lng)).add(b_8.divide(b_2, B_DIV_SCALE, 4).multiply(lng).multiply(lat)).add(b_8.divide(b_2, B_DIV_SCALE, 4).multiply(new BigDecimal(Math.sqrt(Math.abs(lng.doubleValue())))));
     bet = bet.add(b_3.multiply(b_2).multiply(new BigDecimal(Math.sin(b_3.multiply(b_5).multiply(lng).multiply(pi).doubleValue())))
         .add(b_3.multiply(b_2).multiply(new BigDecimal(Math.sin(b_3.multiply(lng).multiply(pi).doubleValue())))).multiply(b_3).divide(b_5, B_DIV_SCALE, 4));
     bet = bet.add(b_3.multiply(b_2).multiply(new BigDecimal(Math.sin(lng.multiply(pi).doubleValue())))
         .add(b_3.multiply(b_3).multiply(b_2).multiply(new BigDecimal(Math.sin(lng.divide(b_5, B_DIV_SCALE, 4).multiply(pi).doubleValue())))).multiply(b_3).divide(b_5, B_DIV_SCALE, 4));
     bet = bet.add(b_9.multiply(new BigDecimal(Math.sin(lng.divide(b_3.multiply(b_3).multiply(b_5), B_DIV_SCALE, 4).multiply(pi).doubleValue())))
         .add(b_9.multiply(b_3).multiply(new BigDecimal(Math.sin(lng.divide(b_5.multiply(b_2), B_DIV_SCALE, 4).multiply(pi).doubleValue())))).multiply(b_3).divide(b_5, B_DIV_SCALE, 4));
     return bet;
   }








   public static boolean out_of_china(BigDecimal lng, BigDecimal lat) {
     if (lng.compareTo(new BigDecimal("73.66")) < 0 || lng.compareTo(new BigDecimal("135.05")) > 0)
       return true;
     if (lat.compareTo(new BigDecimal("3.86")) < 0 || lat.compareTo(new BigDecimal("53.55")) > 0) {
       return true;
     }
     return false;
   }
 }


