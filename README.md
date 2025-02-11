# GRS

This is the official implementation of the GRS cryptography scheme (``From Σ-protocol based Signatures to Ring Signatures: General Construction and Applications``) based on the JPBC library. 

There are three versions provided here to be compatible from older to newer versions of the Java application programming interfaces. Please select a suitable one or modify the ``getObjectSize`` function to use. 

This repository is a part of the [cryptography schemes](https://github.com/BatchClayderman/Cryptography-Schemes). 

## Usage

Please use the ``Start.java`` to launch the program. For example, please right-click the ``Start.java`` and click ``Run as Java application`` if ``eclipse`` is used. 

The file ``Start.java`` implements the following running order. 

- Setup
- KeyGen
- SignI
- VerifyI
- SignII
- VerifyII

## Procedure

### PARS

This contains all parameters in this scheme, which is the fundamental file. 

### Setup

This will set up basic parameters in this scheme. 

### KeyGen

This will generate secret keys and corresponding public keys. 

### SignI

This implements the first signing method. 

### VerifyI

This implements the first verification method. 

### SignII

This implements the second signing method. Please note that you can run ``SignII`` and ``VerifyII`` immediately after running ``VerifyI``. 

If you want to use a new ``PARS`` object to run ``SignII`` and ``VerifyII`` apart from ``SignI`` and ``VerifyI``, please include the following codes. 

```
final int l = pars.getL();
pars = Setup.setup(n);
pars = KeyGen.keyGen(pars);
pars.setL(l);
```

### VerifyII

This implements the second verification method. 
