package com.project.learningkitsupplier.util

import com.tagsamurai.tscomponents.button.OptionData
import com.tagsamurai.tscomponents.model.TreeNode

fun generatedOptionDataString(list: List<String>): List<OptionData<String>> {
    return list.map { OptionData(it,it) }
}

fun getItemName(): List<String> {
    return listOf("Tas","Buku","Laptop")
}

fun getSku(): List<String> {
    return listOf("Tas","Buku","Laptop")
}

fun getCountry(): List<String> {
    return listOf("Kamboja","Malaysia","Indonesia")
}

fun getState(): List<String> {
    return listOf("Tas","Buku","Laptop")
}

fun getCity(): List<String> {
    return listOf("Tas","Buku","Laptop")
}

fun getTransaction(): List<String> {
    return listOf("Reception", "Supplier Return")
}

fun getPic(): List<String> {
    return listOf("John Doe", "Doe John", "Peter Roger", "Scott Parker", "Wanda Stark")
}

fun getGroups(): List<TreeNode<String>> {
    return listOf(
        TreeNode(
            content = "All",
            value = "All",
            children = listOf(
                TreeNode(
                    content = "1st Floor",
                    value = "1st Floor",
                    children = listOf(
                        TreeNode(
                            content = "Room 1",
                            value = "Room 1",
                            children = listOf(
                                TreeNode(
                                    content = "Bed 1",
                                    value = "Bed 1",
                                ),
                                TreeNode(
                                    content = "Bed 2",
                                    value = "Bed 2",
                                )
                            )
                        ),
                        TreeNode(
                            content = "Room 2",
                            value = "Room 2",
                        )
                    )
                ),
                TreeNode(
                    content = "2nd Floor",
                    value = "2nd Floor",
                    children = listOf(
                        TreeNode(
                            content = "Room 3",
                            value = "Room 3",
                        ),
                        TreeNode(
                            content = "Room 4",
                            value = "Room 4",
                            children = listOf(
                                TreeNode(
                                    content = "Bed 3",
                                    value = "Bed 3",
                                ),
                                TreeNode(
                                    content = "Bed 4",
                                    value = "Bed 4",
                                )
                            )
                        )
                    )
                ),
                TreeNode(
                    content = "3rd Floor",
                    value = "3rd Floor",
                )
            )
        )
    )
}