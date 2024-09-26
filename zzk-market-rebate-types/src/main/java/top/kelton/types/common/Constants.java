package top.kelton.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {

        SUCCESS("0000", "成功"),
        UN_ERROR("0001", "未知失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        ;

        private String code;
        private String info;

    }



    public static class RedisKey {
        public static String STRATEGY_KEY = "zzk_market_rebate:strategy_key_";
        public static String STRATEGY_AWARD_KEY = "zzk_market_rebate:strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "zzk_market_rebate:strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "zzk_market_rebate:strategy_rate_range_key_";
    }

}
