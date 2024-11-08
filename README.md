# Shamir Secret Sharing Algorithm

This repository implements the Shamir Secret Sharing (SSS) Algorithm, a cryptographic technique that allows a "secret" to be split into multiple shares, where only a subset of the shares is needed to reconstruct the original secret. This technique is widely used for secure data storage, multi-party security systems, and more.

## Introduction

Shamir's Secret Sharing Algorithm divides a secret into `n` shares, where any subset of `k` shares can reconstruct the original secret, but fewer than `k` shares reveal no information. This is achieved using polynomial interpolation over a finite field.

For example, given a secret `S`, we create a random polynomial of degree `k-1` with `S` as the constant term, and use the polynomial to generate `n` points. Any `k` points can then be used to reconstruct the polynomial and retrieve the original secret.


## Features

- **Secret Splitting**: Split any secret (e.g., a number or text) into multiple shares.
- **Threshold Reconstruction**: Reconstruct the secret using a subset of shares, with the threshold number of shares required for recovery.
- **Configurable Parameters**: Specify the number of shares and threshold required for reconstruction.
- **Simple and Secure**: Implements Shamir's polynomial-based algorithm, which is mathematically secure.

