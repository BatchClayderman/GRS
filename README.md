# GRS

This is an implementation of the GRS scheme based on the JPBC library. 

Please right-click the ``Start.java`` and click ``Run as Java application``. 

The file ``Start.java`` implements the following running order. 

- Setup
- KeyGen
- SignI
- VerifyI
- SignII
- VerifyII

## PARS

This contains all parameters in this scheme, which is the fundamental file. 

## Setup

This will set up basic parameters in this scheme. 

## KeyGen

This will generate secret keys and corresponding public keys. 

## SignI

This implements the first signing method. 

## VerifyI

This implements the first verification method. 

## SignII

This implements the second signing method. Please note that you can run ``SignII`` and ``VerifyII`` immediately after running ``VerifyI``. 

If you want to use a new ``PARS`` object to run ``SignII`` and ``VerifyII`` apart from ``SignI`` and ``VerifyI``, please include the following codes. 

```
int l = pars.getL();
pars = Setup.setup(n);
pars = KeyGen.keyGen(pars);
pars.setL(l);
```

## VerifyII

This implements the second verification method. 
