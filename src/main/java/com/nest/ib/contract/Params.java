package com.nest.ib.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.generated.Uint160;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;

import java.math.BigInteger;

/**
 * The contract parameters
 */
public class Params extends StaticStruct {

    public Params(BigInteger miningEthUnit,
                  BigInteger nestStakedNum1k,
                  BigInteger biteFeeRate,
                  BigInteger miningFeeRate,
                  BigInteger priceDurationBlock,
                  BigInteger maxBiteNestedLevel,
                  BigInteger biteInflateFactor,
                  BigInteger biteNestInflateFactor) {
        super(new Uint8(miningEthUnit),
                new Uint32(nestStakedNum1k),
                new Uint8(biteFeeRate),
                new Uint8(miningFeeRate),
                new Uint8(priceDurationBlock),
                new Uint8(maxBiteNestedLevel),
                new Uint8(biteInflateFactor),
                new Uint8(biteNestInflateFactor));

        this.miningEthUnit = miningEthUnit;
        this.nestStakedNum1k = nestStakedNum1k;
        this.biteFeeRate = biteFeeRate;
        this.miningFeeRate = miningFeeRate;
        this.priceDurationBlock = priceDurationBlock;
        this.maxBiteNestedLevel = maxBiteNestedLevel;
        this.biteInflateFactor = biteInflateFactor;
        this.biteNestInflateFactor = biteNestInflateFactor;
    }

    public Params(Uint160 miningEthUnit,
                  Uint32 nestStakedNum1k,
                  Uint32 biteFeeRate,
                  Uint32 miningFeeRate,
                  Uint8 priceDurationBlock,
                  Uint8 maxBiteNestedLevel,
                  Uint32 biteInflateFactor,
                  Uint32 biteNestInflateFactor) {
        super(miningEthUnit,
                nestStakedNum1k,
                biteFeeRate,
                miningFeeRate,
                priceDurationBlock,
                maxBiteNestedLevel,
                biteInflateFactor,
                biteNestInflateFactor);

        this.miningEthUnit = miningEthUnit.getValue();
        this.nestStakedNum1k = nestStakedNum1k.getValue();
        this.biteFeeRate = biteFeeRate.getValue();
        this.miningFeeRate = miningFeeRate.getValue();
        this.priceDurationBlock = priceDurationBlock.getValue();
        this.maxBiteNestedLevel = maxBiteNestedLevel.getValue();
        this.biteInflateFactor = biteInflateFactor.getValue();
        this.biteNestInflateFactor = biteNestInflateFactor.getValue();
    }

    // Minimal quotation (Currently the initial quotation of Ethnum is only allowed to be equal to the minimum quotation)
    public BigInteger miningEthUnit;
    // Freezing the number of NEST related factors
    public BigInteger nestStakedNum1k;
    // Eat a single commission percentage
    public BigInteger biteFeeRate;
    // Mining takes a percentage
    public BigInteger miningFeeRate;
    // The number of blocks in the price stability zone
    public BigInteger priceDurationBlock;
    // This parameter is used to divide the amount of money that needs to be frozen when eating the order
    public BigInteger maxBiteNestedLevel;
    // Eat single frozen ETH factor: eat single price double multiple
    public BigInteger biteInflateFactor;
    // Eat order freeze Nest scale factor: eat order price double
    public BigInteger biteNestInflateFactor;

    public BigInteger getMiningEthUnit() {
        return miningEthUnit;
    }

    public void setMiningEthUnit(BigInteger miningEthUnit) {
        this.miningEthUnit = miningEthUnit;
    }

    public BigInteger getNestStakedNum1k() {
        return nestStakedNum1k;
    }

    public void setNestStakedNum1k(BigInteger nestStakedNum1k) {
        this.nestStakedNum1k = nestStakedNum1k;
    }

    public BigInteger getBiteFeeRate() {
        return biteFeeRate;
    }

    public void setBiteFeeRate(BigInteger biteFeeRate) {
        this.biteFeeRate = biteFeeRate;
    }

    public BigInteger getMiningFeeRate() {
        return miningFeeRate;
    }

    public void setMiningFeeRate(BigInteger miningFeeRate) {
        this.miningFeeRate = miningFeeRate;
    }

    public BigInteger getPriceDurationBlock() {
        return priceDurationBlock;
    }

    public void setPriceDurationBlock(BigInteger priceDurationBlock) {
        this.priceDurationBlock = priceDurationBlock;
    }

    public BigInteger getMaxBiteNestedLevel() {
        return maxBiteNestedLevel;
    }

    public void setMaxBiteNestedLevel(BigInteger maxBiteNestedLevel) {
        this.maxBiteNestedLevel = maxBiteNestedLevel;
    }

    public BigInteger getBiteInflateFactor() {
        return biteInflateFactor;
    }

    public void setBiteInflateFactor(BigInteger biteInflateFactor) {
        this.biteInflateFactor = biteInflateFactor;
    }

    public BigInteger getBiteNestInflateFactor() {
        return biteNestInflateFactor;
    }

    public void setBiteNestInflateFactor(BigInteger biteNestInflateFactor) {
        this.biteNestInflateFactor = biteNestInflateFactor;
    }
}
