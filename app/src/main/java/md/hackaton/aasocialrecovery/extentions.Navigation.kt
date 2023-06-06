package md.hackaton.aasocialrecovery

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph


fun Int?.orEmpty(default: Int = 0): Int {
    return this ?: default
}

fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}

fun NavController.navigateSafe(direction: NavDirections) {
    val destinationId = currentDestination?.getAction(direction.actionId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(direction.actionId, direction.arguments) }
        }
    }
}