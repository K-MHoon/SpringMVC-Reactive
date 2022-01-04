package com.kmhoon.reactive.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemUnitTest {

    @Test
    @DisplayName("Item 도메인 검증")
    void itemBasicsShouldWork() {
        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
        Item sampleItem2 = new Item("item1", "TV tray", "Alf TV tray", 19.99);

        assertThat(sampleItem.getId()).isEqualTo("item1");
        assertThat(sampleItem.getName()).isEqualTo("TV tray");
        assertThat(sampleItem.getDescription()).isEqualTo("Alf TV tray");
        assertThat(sampleItem.getPrice()).isEqualTo(19.99);

        assertThat(sampleItem).isEqualTo(sampleItem2);
    }

}