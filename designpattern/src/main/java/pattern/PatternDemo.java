package pattern;

import java.util.regex.Pattern;

public class PatternDemo {

/*
3.库表路由规则
mapping:
- TICK_SHFE:(cu|al|pb|zn|)*_t_* (cu1909_t_atp,cu1909_t_esunny)
- TICK_DCE:(a|al|pb|zn|)*_t_*
- TICK_CALC_BASIC:*_cross_t
- KM_SHFE:cu*_*min|d|w|m|q|y
- KM_CALC_CROSS:*_cross_*min|d|w|m|q|y
*/

    public static void main(String[] args) {

//        testTick();
//        testKM();
        testCalcTick();

    }

    // $ 表示结尾；"\w":匹配任何字类字符，包括下划线与"[A-Za-z0-9_]"等效。;
    public static void testTick() {
        Pattern p = Pattern.compile("(cu|ni)*_t_[A-Za-z0-9]+$");    // tick_shfe
//        Pattern p = Pattern.compile("(cu|ni)*_t_\\w+$");    // tick_shfe
        boolean matches1 = p.matcher("cu1906").find();
        boolean matches2 = p.matcher("cu1906_1t_atpsf").find();
        boolean matches3 = p.matcher("ni1909=_t_reuter").find();
        boolean matches4 = p.matcher("ni1909_t__reuter").find();
        System.out.println(matches1);
        System.out.println(matches2);
        System.out.println(matches3);
        System.out.println(matches4);
    }

    public static void testCalcTick(){
        Pattern p = Pattern.compile("^*[A-Za-z0-9]_basis_t$");    // tick_calc_basis
        boolean matches1 = p.matcher("calculationId_basis_t_").find();
        boolean matches2 = p.matcher("calculationId_1basis_t").find();
        boolean matches3 = p.matcher("calculationId__basis_t").find();
        boolean matches4 = p.matcher("calculationId_basis_t").find();
        System.out.println(matches1);
        System.out.println(matches2);
        System.out.println(matches3);
        System.out.println(matches4);

    }

    public static void testKM() {
        Pattern p = Pattern.compile("(cu|ni)*_(minute|\\d+min|d|w|m|q|y)$");    // km_shfe
        boolean matches1 = p.matcher("cu1906_minute").find();
        boolean matches2 = p.matcher("cu1906_ddd").find();
        boolean matches3 = p.matcher("cu1906_min").find();
        boolean matches4 = p.matcher("cu1906_1min").find();
        boolean matches5 = p.matcher("cu1906_15min").find();
        System.out.println(matches1);
        System.out.println(matches2);
        System.out.println(matches3);
        System.out.println(matches4);
        System.out.println(matches5);
    }
}
