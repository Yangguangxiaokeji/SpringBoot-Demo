package com.foogui.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 键值对
 *
 * @author Foogui
 * @date 2023/07/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyAndValue<K, V> {
    private K key;

    private V value;

}
