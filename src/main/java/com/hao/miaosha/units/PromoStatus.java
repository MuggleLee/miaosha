package com.hao.miaosha.units;

/**
 * @author MuggleLee
 * @date 2020/5/1
 */
public enum PromoStatus {
    NO_PROMO(0),PROMO_NOT_START(1),PROMO_STARTING(2),PROMO_END(3);

    private int status;

    PromoStatus(int status) {
        this.status = status;
    }

    public int getPromoStatus(){
        return status;
    }

}
