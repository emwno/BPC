package com.aashir.bpc.intro;

import java.util.Set;

public interface IntroInterface {
    void onLogin(String username, String password);

    void onClassPick(int aclass);

    void onSocietyPick(String society);

    void onSubjectPick(Set<String> subjects);
}
