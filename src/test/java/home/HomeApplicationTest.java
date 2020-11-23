package home;

import org.junit.jupiter.api.Test;

class HomeApplicationTest {

    @Test
    void main() {
        HomeApplication.main(new String[]{"--server.port=8088"});
    }
}