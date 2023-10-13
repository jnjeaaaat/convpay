package com.zerobase.convpay.service;

import com.zerobase.convpay.dto.PayCancelRequest;
import com.zerobase.convpay.dto.PayCancelResponse;
import com.zerobase.convpay.type.ConvenienceType;
import com.zerobase.convpay.dto.PayRequest;
import com.zerobase.convpay.dto.PayResponse;
import com.zerobase.convpay.type.PayCancelResult;
import com.zerobase.convpay.type.PayMethodType;
import com.zerobase.convpay.type.PayResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ConveniencePayServiceTest {
    ConveniencePayService conveniencePayService = new ConveniencePayService(
            new HashSet<>(
                    Arrays.asList(new MoneyAdapter(), new CardAdapter())
            ),
            new DiscountByPayMethod()
    );

    @Test
    void 결제성공() {
        //given
        PayRequest payRequest = new PayRequest(PayMethodType.MONEY, ConvenienceType.G25, 1000_000);

        //when
        PayResponse payResponse = conveniencePayService.pay(payRequest);

        //then
        assertEquals(PayResult.SUCCESS, payResponse.getPayResult());
        assertEquals(700_000, payResponse.getPaidAmount());
    }

    @Test
    void 결제실패() {
        //given
        PayRequest payRequest = new PayRequest(PayMethodType.MONEY, ConvenienceType.G25, 1000_001);

        //when
        PayResponse payResponse = conveniencePayService.pay(payRequest);

        //then
        assertEquals(PayResult.FAIL, payResponse.getPayResult());
        assertEquals(0, payResponse.getPaidAmount());
    }

    @Test
    void 결제취소_성공() {
        //given
        PayCancelRequest payCancelRequest= new PayCancelRequest(PayMethodType.MONEY, ConvenienceType.GU, 101);

        //when
        PayCancelResponse payCancelResponse = conveniencePayService.payCancel(payCancelRequest);

        //then
        assertEquals(PayCancelResult.PAY_CANCEL_SUCCESS, payCancelResponse.getPayCancelResult());
        assertEquals(payCancelRequest.getPayCancelAmount(), payCancelResponse.getPayCanceledAmount());
    }

    @Test
    void 결제취소_실패() {
        //given
        PayCancelRequest payCancelRequest= new PayCancelRequest(PayMethodType.MONEY, ConvenienceType.GU, 50);

        //when
        PayCancelResponse payCancelResponse = conveniencePayService.payCancel(payCancelRequest);

        //then
        assertEquals(PayCancelResult.PAY_CANCEL_FAIL, payCancelResponse.getPayCancelResult());
        assertEquals(0, payCancelResponse.getPayCanceledAmount());
    }

}