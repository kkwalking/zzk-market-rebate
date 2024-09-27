package top.kelton.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";


    public static class RedisKey {
        public static String STRATEGY_KEY = "zzk_market_rebate:strategy_key_";
        public static String STRATEGY_AWARD_KEY = "zzk_market_rebate:strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "zzk_market_rebate:strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "zzk_market_rebate:strategy_rate_range_key_";
    }

}
