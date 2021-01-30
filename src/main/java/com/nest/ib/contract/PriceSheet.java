package com.nest.ib.contract;

import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;

import java.math.BigInteger;

/**
 * Quotation data
 */
public class PriceSheet extends StaticStruct {

    public PriceSheet(BigInteger miner,
                      BigInteger height,
                      BigInteger ethNum,
                      BigInteger remainNum,

                      BigInteger level,
                      BigInteger typ,
                      BigInteger state,
                      BigInteger index,
                      BigInteger nestNum1k,
                      BigInteger tokenAmountPerEth) {
        super(new Uint160(miner),
                new Uint32(height),
                new Uint32(ethNum),
                new Uint32(remainNum),

                new Uint8(level),
                new Uint8(typ),
                new Uint8(state),
                new Uint256(index),
                new Uint32(nestNum1k),
                new Uint128(tokenAmountPerEth));

        this.miner = new Address(miner);
        this.height = height;
        this.ethNum = ethNum;
        this.remainNum = remainNum;
        this.level = level;
        this.typ = typ;
        this.state = state;
        this.index = index;
        this.nestNum1k = nestNum1k;
        this.tokenAmountPerEth = tokenAmountPerEth;
    }

    public PriceSheet(Uint160 miner,
                      Uint32 height,
                      Uint32 ethNum,
                      Uint32 remainNum,

                      Uint8 level,
                      Uint8 typ,
                      Uint8 state,
                      Uint256 index,
                      Uint32 nestNum1k,
                      Uint128 tokenAmountPerEth) {
        super(miner,
                height,
                ethNum,
                remainNum,

                level,
                typ,
                state,
                index,
                nestNum1k,
                tokenAmountPerEth);

        this.miner = new Address(miner);
        this.height = height.getValue();
        this.ethNum = ethNum.getValue();
        this.remainNum = remainNum.getValue();
        this.level = level.getValue();
        this.typ = typ.getValue();
        this.state = state.getValue();
        this.index = index.getValue();
        this.nestNum1k = nestNum1k.getValue();
        this.tokenAmountPerEth = tokenAmountPerEth.getValue();
    }

    // Quotator address
    public Address miner;
    // The height of the block where the quotation is located
    public BigInteger height;
    // Amount of ETH stored by the bidder at the start of the quote
    public BigInteger ethNum;
    // How much ETH/ Token can be eaten? As long as it is eaten, its value will decrease. When it reaches 0, the quotation can no longer be eaten
    public BigInteger remainNum;
    // Record the eating state, and when the value is 1-4, freeze twice the ETH, and when the value is 5-127, freeze twice the NEST
    public BigInteger level;
    // 1: USD | 2: NEST | 3: TOKEN
    public BigInteger typ;
    // 0: closed | 1: posted | 2: bitten
    public BigInteger state;
    // Index corresponding to quotation sheet
    public BigInteger index;
    /**
     * Quote 1 ETH for each time, corresponding to the number of Nest to be frozen.
     * NestNum1K is a freezing Nest factor, which can be multiplied by 1000 to get the number of Nest frozen for this quotation/order.
     * This value will change when the order is eaten.
     */
    public BigInteger nestNum1k;
    // 1 Number of ETH exchangeable tokens (price)
    public BigInteger tokenAmountPerEth;


    public Address getMiner() {
        return miner;
    }

    public void setMiner(Address miner) {
        this.miner = miner;
    }

    public BigInteger getHeight() {
        return height;
    }

    public void setHeight(BigInteger height) {
        this.height = height;
    }

    public BigInteger getEthNum() {
        return ethNum;
    }

    public void setEthNum(BigInteger ethNum) {
        this.ethNum = ethNum;
    }

    public BigInteger getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(BigInteger remainNum) {
        this.remainNum = remainNum;
    }

    public BigInteger getLevel() {
        return level;
    }

    public void setLevel(BigInteger level) {
        this.level = level;
    }

    public BigInteger getTyp() {
        return typ;
    }

    public void setTyp(BigInteger typ) {
        this.typ = typ;
    }

    public BigInteger getState() {
        return state;
    }

    public void setState(BigInteger state) {
        this.state = state;
    }

    public BigInteger getNestNum1k() {
        return nestNum1k;
    }

    public void setNestNum1k(BigInteger nestNum1k) {
        this.nestNum1k = nestNum1k;
    }

    public BigInteger getTokenAmountPerEth() {
        return tokenAmountPerEth;
    }

    public void setTokenAmountPerEth(BigInteger tokenAmountPerEth) {
        this.tokenAmountPerEth = tokenAmountPerEth;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }
}
