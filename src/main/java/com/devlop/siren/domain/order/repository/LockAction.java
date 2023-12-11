package com.devlop.siren.domain.order.repository;

@FunctionalInterface
public interface LockAction {
  void perform();
}
