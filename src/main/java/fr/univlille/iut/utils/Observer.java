package fr.univlille.iut.utils;

public interface Observer {

    void update(Observable subj);
    void update(Observable subj, Object data);

}