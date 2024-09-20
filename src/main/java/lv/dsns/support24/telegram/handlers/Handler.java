package lv.dsns.support24.telegram.handlers;

public interface Handler<T> {
    void choose(T t);
}