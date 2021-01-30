package com.nest.ib.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.*;

import java.math.BigInteger;

public class PriceSheetPub extends StaticStruct {

    public PriceSheetPub(BigInteger miner,
                         BigInteger height,
                         BigInteger ethNum,

                         BigInteger typ,
                         BigInteger state,
                         BigInteger ethNumBal,
                         BigInteger tokenNumBal) {
        super(new Uint160(miner),
                new Uint32(height),
                new Uint32(ethNum),

                new Uint8(typ),
                new Uint8(state),
                new Uint32(ethNumBal),
                new Uint32(tokenNumBal));

        this.miner =new Address(miner);
        this.height = height;
        this.ethNum = ethNum;

        this.typ = typ;
        this.state = state;
        this.ethNumBal = ethNumBal;
        this.tokenNumBal = tokenNumBal;
    }

    public PriceSheetPub(Uint160 miner,
                         Uint32 height,
                         Uint32 ethNum,

                         Uint8 typ,
                         Uint8 state,
                         Uint32 ethNumBal,
                         Uint32 tokenNumBal) {
        super(miner,
                height,
                ethNum,

                typ,
                state,
                ethNumBal,
                tokenNumBal);

        this.miner = new Address(miner);
        this.height = height.getValue();
        this.ethNum = ethNum.getValue();

        this.typ = typ.getValue();
        this.state = state.getValue();
        this.ethNumBal = ethNumBal.getValue();
        this.tokenNumBal = tokenNumBal.getValue();
    }
    // Quotator address
    public Address miner;
    // The height of the block where the quotation is located
    public BigInteger height;
    // Amount of ETH deposited by the bidder at the beginning of the quotation
    public BigInteger ethNum;
    // 1: USD | 2: NEST | 3: TOKEN
    public BigInteger typ;
    // 0: closed | 1: posted | 2: bitten
    public BigInteger state;
    // It is used to record the change of the quotation ETH quantity, and settle the remaining ETH funds when the quotation is closed
    public BigInteger ethNumBal;
    // It is used to record the change of the number of tokens on the quotation sheet and settle the remaining token funds when the quotation sheet is closed
    public BigInteger tokenNumBal;

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

    public BigInteger getEthNumBal() {
        return ethNumBal;
    }

    public void setEthNumBal(BigInteger ethNumBal) {
        this.ethNumBal = ethNumBal;
    }

    public BigInteger getTokenNumBal() {
        return tokenNumBal;
    }

    public void setTokenNumBal(BigInteger tokenNumBal) {
        this.tokenNumBal = tokenNumBal;
    }
}
