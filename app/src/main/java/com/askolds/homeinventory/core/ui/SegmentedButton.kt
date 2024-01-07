package com.askolds.homeinventory.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.IconSpacing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.MultiContentMeasurePolicy
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * <a href="https://m3.material.io/components/segmented-buttons/overview" class="external" target="_blank">Material Segmented Button</a>.
 * Segmented buttons help people select options, switch views, or sort elements.
 *
 * A default Toggleable Segmented Button. Also known as Outlined Segmented Button.
 * See [Modifier.selectable].
 *
 * Selectable segmented buttons should be used for cases where the selection is mutually
 * exclusive, when only one button can be selected at a time.
 *
 * This should typically be used inside of a [SegmentedButtonRow]
 *
 * For a sample showing Segmented button with only checked icons see:
 * @sample androidx.compose.material3.samples.SegmentedButtonSingleSelectSample
 *
 * @param selected whether this button is selected or not
 * @param onClick callback to be invoked when the button is clicked.
 * therefore the change of checked state in requested.
 * @param shape the shape for this button
 * @param modifier the [Modifier] to be applied to this button
 * @param enabled controls the enabled state of this button. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param colors [SegmentedButtonColors] that will be used to resolve the colors used for this
 * @param border the border for this button, see [SegmentedButtonColors]
 * Button in different states
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this button in different states.
 * @param icon the icon slot for this button, you can pass null in unchecked, in which case
 * the content will displace to show the checked icon, or pass different icon lambdas for
 * unchecked and checked in which case the icons will crossfade.
 * @param label content to be rendered inside this button
 */
@Composable
fun SegmentedButtonRowScope.SegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SegmentedButtonColors = SegmentedButtonDefaults.colors(),
    border: BorderStroke = SegmentedButtonDefaults.borderStroke(
        colors.borderColor(enabled, selected)
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: @Composable () -> Unit = { SegmentedButtonDefaults.Icon(selected) },
    label: @Composable () -> Unit,
) {
    val containerColor = colors.containerColor(enabled, selected)
    val contentColor = colors.contentColor(enabled, selected)
    val interactionCount = interactionSource.interactionCountAsState()

    Surface(
        modifier = modifier
            .weight(1f)
            .interactionZIndex(selected, interactionCount)
            .defaultMinSize(
                minWidth = ButtonDefaults.MinWidth,
                minHeight = ButtonDefaults.MinHeight
            )
            .semantics { role = Role.RadioButton },
        selected = selected,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        interactionSource = interactionSource
    ) {
        SegmentedButtonContent(icon, label)
    }
}

/**
 * <a href="https://m3.material.io/components/segmented-buttons/overview" class="external" target="_blank">Material Segmented Button</a>.
 *
 * A Layout to correctly position and size [SegmentedButton]s in a Row.
 * It handles overlapping items so that strokes of the item are correctly on top of each other.
 * [SingleChoiceSegmentedButtonRow] is used when the selection only allows one value, for correct
 * semantics.
 *
 * @sample androidx.compose.material3.samples.SegmentedButtonSingleSelectSample
 *
 * @param modifier the [Modifier] to be applied to this row
 * @param space the dimension of the overlap between buttons. Should be equal to the stroke width
 *  used on the items.
 * @param content the content of this Segmented Button Row, typically a sequence of
 * [SegmentedButton]s
 */
@Composable
fun SegmentedButtonRow(
    modifier: Modifier = Modifier,
    space: Dp = SegmentedButtonDefaults.BorderWidth,
    content: @Composable SegmentedButtonRowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .selectableGroup()
            .defaultMinSize(minHeight = 40.dp)
            .width(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(-space),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scope = remember { SegmentedButtonScopeWrapper(this) }
        scope.content()
    }
}

@Composable
private fun SegmentedButtonContent(
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(ButtonDefaults.TextButtonContentPadding)
    ) {
        val typography = MaterialTheme.typography.labelLarge
        ProvideTextStyle(typography) {
            val scope = rememberCoroutineScope()
            val measurePolicy = remember { SegmentedButtonContentMeasurePolicy(scope) }

            Layout(
                modifier = Modifier.height(IntrinsicSize.Min),
                contents = listOf(icon, content),
                measurePolicy = measurePolicy
            )
        }
    }
}

internal class SegmentedButtonContentMeasurePolicy(
    val scope: CoroutineScope
) : MultiContentMeasurePolicy {
    var animatable: Animatable<Int, AnimationVector1D>? = null
    private var initialOffset: Int? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun MeasureScope.measure(
        measurables: List<List<Measurable>>,
        constraints: Constraints
    ): MeasureResult {
        val (iconMeasurables, contentMeasurables) = measurables
        val iconPlaceables = iconMeasurables.fastMap { it.measure(constraints) }
        val iconWidth = iconPlaceables.fastMaxBy { it.width }?.width ?: 0
        val contentPlaceables = contentMeasurables.fastMap { it.measure(constraints) }
        val contentWidth = contentPlaceables.fastMaxBy { it.width }?.width
        val height = contentPlaceables.fastMaxBy { it.height }?.height ?: 0
        val width = maxOf(SegmentedButtonDefaults.IconSize.roundToPx(), iconWidth) +
                IconSpacing.roundToPx() +
                (contentWidth ?: 0)
        val offsetX = if (iconWidth == 0) {
            -(SegmentedButtonDefaults.IconSize.roundToPx() + IconSpacing.roundToPx()) / 2
        } else {
            0
        }

        if (initialOffset == null) {
            initialOffset = offsetX
        } else {
            val anim = animatable ?: Animatable(initialOffset!!, Int.VectorConverter)
                .also { animatable = it }
            if (anim.targetValue != offsetX) {
                scope.launch {
                    anim.animateTo(offsetX, tween(350))
                }
            }
        }

        return layout(width, height) {
            iconPlaceables.fastForEach {
                it.place(0, (height - it.height) / 2)
            }

            val contentOffsetX = SegmentedButtonDefaults.IconSize.roundToPx() +
                    IconSpacing.roundToPx() + (animatable?.value ?: offsetX)

            contentPlaceables.fastForEach {
                it.place(
                    contentOffsetX,
                    (height - it.height) / 2
                )
            }
        }
    }
}

private fun Modifier.interactionZIndex(checked: Boolean, interactionCount: State<Int>) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val zIndex = interactionCount.value + if (checked) CheckedZIndexFactor else 0f
            placeable.place(0, 0, zIndex)
        }
    }

@Composable
private fun InteractionSource.interactionCountAsState(): State<Int> {
    val interactionCount = remember { mutableIntStateOf(0) }
    LaunchedEffect(this) {
        this@interactionCountAsState.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press,
                is FocusInteraction.Focus -> {
                    interactionCount.intValue++
                }

                is PressInteraction.Release,
                is FocusInteraction.Unfocus,
                is PressInteraction.Cancel -> {
                    interactionCount.intValue--
                }
            }
        }
    }

    return interactionCount
}

private const val CheckedZIndexFactor = 5f
private val IconSpacing = 8.dp

interface SegmentedButtonRowScope : RowScope

private class SegmentedButtonScopeWrapper(scope: RowScope) :
    SegmentedButtonRowScope, RowScope by scope

/* Contains defaults to be used with [SegmentedButtonRow] and [SegmentedButton] */
@Stable
object SegmentedButtonDefaults {
    /**
     * Creates a [SegmentedButtonColors] that represents the different colors
     * used in a [SegmentedButton] in different states.
     *
     * @param activeContainerColor the color used for the container when enabled and active
     * @param activeContentColor the color used for the content when enabled and active
     * @param activeBorderColor the color used for the border when enabled and active
     * @param inactiveContainerColor the color used for the container when enabled and inactive
     * @param inactiveContentColor the color used for the content when enabled and inactive
     * @param inactiveBorderColor the color used for the border when enabled and active
     * @param disabledActiveContainerColor the color used for the container
     * when disabled and active
     * @param disabledActiveContentColor the color used for the content when disabled and active
     * @param disabledActiveBorderColor the color used for the border when disabled and active
     * @param disabledInactiveContainerColor the color used for the container
     * when disabled and inactive
     * @param disabledInactiveContentColor the color used for the content when disabled and
     * unchecked
     * @param disabledInactiveBorderColor the color used for the border when disabled and inactive
     */
    @Composable
    fun colors(
        activeContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer, //SelectedContainerColor.value,
        activeContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        activeBorderColor: Color = MaterialTheme.colorScheme.outline,
        inactiveContainerColor: Color = MaterialTheme.colorScheme.surface,
        inactiveContentColor: Color = MaterialTheme.colorScheme.onSurface,
        inactiveBorderColor: Color = activeBorderColor,
        disabledActiveContainerColor: Color = activeContainerColor,
        disabledActiveContentColor: Color = MaterialTheme.colorScheme.onSurface
            .copy(alpha = 0.38f),
        disabledActiveBorderColor: Color = MaterialTheme.colorScheme.outline
            .copy(alpha = 0.12f),
        disabledInactiveContainerColor: Color = inactiveContainerColor,
        disabledInactiveContentColor: Color = disabledActiveContentColor,
        disabledInactiveBorderColor: Color = activeBorderColor,
    ): SegmentedButtonColors = SegmentedButtonColors(
        activeContainerColor = activeContainerColor,
        activeContentColor = activeContentColor,
        activeBorderColor = activeBorderColor,
        inactiveContainerColor = inactiveContainerColor,
        inactiveContentColor = inactiveContentColor,
        inactiveBorderColor = inactiveBorderColor,
        disabledActiveContainerColor = disabledActiveContainerColor,
        disabledActiveContentColor = disabledActiveContentColor,
        disabledActiveBorderColor = disabledActiveBorderColor,
        disabledInactiveContainerColor = disabledInactiveContainerColor,
        disabledInactiveContentColor = disabledInactiveContentColor,
        disabledInactiveBorderColor = disabledInactiveBorderColor
    )

    /**
     * The shape of the segmented button container, for correct behavior this should or the desired
     * [CornerBasedShape] should be used with [itemShape] and passed to each segmented button.
     */
    val baseShape: CornerBasedShape
        @Composable
        @ReadOnlyComposable
        get() = CircleShape

    /** Default border width used in segmented button */
    val BorderWidth: Dp = 1.dp

    /**
     * A shape constructor that the button in [index] should have when there are [count] buttons in
     * the container.
     *
     * @param index the index for this button in the row
     * @param count the count of buttons in this row
     * @param baseShape the [CornerBasedShape] the base shape that should be used in buttons that
     * are not in the start or the end.
     */
    @Composable
    @ReadOnlyComposable
    fun itemShape(index: Int, count: Int, baseShape: CornerBasedShape = SegmentedButtonDefaults.baseShape): Shape {
        if (count == 1) {
            return baseShape
        }

        return when (index) {
            0 -> baseShape.start()
            count - 1 -> baseShape.end()
            else -> RectangleShape
        }
    }

    /** Helper function for component shape tokens. Used to grab the start values of a shape parameter. */
    private fun CornerBasedShape.start(): CornerBasedShape {
        return copy(topEnd = CornerSize(0.0.dp), bottomEnd = CornerSize(0.0.dp))
    }

    /** Helper function for component shape tokens. Used to grab the end values of a shape parameter. */
    internal fun CornerBasedShape.end(): CornerBasedShape {
        return copy(topStart = CornerSize(0.0.dp), bottomStart = CornerSize(0.0.dp))
    }

    /**
     * Icon size to use for icons used in [SegmentedButton]
     */
    val IconSize = 18.dp

    /** And icon to indicate the segmented button is checked or selected */
    @Composable
    fun ActiveIcon() {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            modifier = Modifier.size(IconSize)
        )
    }

    /**
     * The default implementation of icons for Segmented Buttons.
     *
     * @param active whether the button is activated or not.
     * @param activeContent usually a checkmark icon of [IconSize] dimensions.
     * @param inactiveContent typically an icon of [IconSize]. It shows only when the button is not
     * checked.
     */
    @Composable
    fun Icon(
        active: Boolean,
        activeContent: @Composable () -> Unit = { ActiveIcon() },
        inactiveContent: (@Composable () -> Unit)? = null
    ) {
        if (inactiveContent == null) {
            AnimatedVisibility(
                visible = active,
                exit = ExitTransition.None,
                enter = fadeIn(tween(350)) + scaleIn(
                    initialScale = 0f,
                    transformOrigin = TransformOrigin(0f, 1f),
                    animationSpec = tween(350),
                ),
            ) {
                activeContent()
            }
        } else {
            Crossfade(targetState = active) {
                if (it) activeContent() else inactiveContent()
            }
        }
    }

    /**
     * Default factory for Segmented Button [BorderStroke] can be customized through [width],
     * and [color]. When using a width different than default make sure to also update
     * [MultiChoiceSegmentedButtonRow] or [SingleChoiceSegmentedButtonRow] space param.
     */
    fun borderStroke(
        color: Color,
        width: Dp = BorderWidth,
    ): BorderStroke = BorderStroke(width = width, color = color)
}

/**
 * The different colors used in parts of the [SegmentedButton] in different states
 *
 * @constructor create an instance with arbitrary colors, see [SegmentedButtonDefaults] for a
 * factory method using the default material3 spec
 *
 * @param activeContainerColor the color used for the container when enabled and active
 * @param activeContentColor the color used for the content when enabled and active
 * @param activeBorderColor the color used for the border when enabled and active
 * @param inactiveContainerColor the color used for the container when enabled and inactive
 * @param inactiveContentColor the color used for the content when enabled and inactive
 * @param inactiveBorderColor the color used for the border when enabled and active
 * @param disabledActiveContainerColor the color used for the container when disabled and active
 * @param disabledActiveContentColor the color used for the content when disabled and active
 * @param disabledActiveBorderColor the color used for the border when disabled and active
 * @param disabledInactiveContainerColor the color used for the container
 * when disabled and inactive
 * @param disabledInactiveContentColor the color used for the content when disabled and inactive
 * @param disabledInactiveBorderColor the color used for the border when disabled and inactive
 */
@Immutable
class SegmentedButtonColors(
    // enabled & active
    val activeContainerColor: Color,
    val activeContentColor: Color,
    val activeBorderColor: Color,
    // enabled & inactive
    val inactiveContainerColor: Color,
    val inactiveContentColor: Color,
    val inactiveBorderColor: Color,
    // disable & active
    val disabledActiveContainerColor: Color,
    val disabledActiveContentColor: Color,
    val disabledActiveBorderColor: Color,
    // disable & inactive
    val disabledInactiveContainerColor: Color,
    val disabledInactiveContentColor: Color,
    val disabledInactiveBorderColor: Color
) {
    /**
     * Represents the color used for the SegmentedButton's border,
     * depending on [enabled] and [active].
     *
     * @param enabled whether the [SegmentedButton] is enabled or not
     * @param active whether the [SegmentedButton] item is checked or not
     */
    @Stable
    internal fun borderColor(enabled: Boolean, active: Boolean): Color {
        return when {
            enabled && active -> activeBorderColor
            enabled && !active -> inactiveBorderColor
            !enabled && active -> disabledActiveBorderColor
            else -> disabledInactiveBorderColor
        }
    }

    /**
     * Represents the content color passed to the items
     *
     * @param enabled whether the [SegmentedButton] is enabled or not
     * @param checked whether the [SegmentedButton] item is checked or not
     */
    @Stable
    internal fun contentColor(enabled: Boolean, checked: Boolean): Color {
        return when {
            enabled && checked -> activeContentColor
            enabled && !checked -> inactiveContentColor
            !enabled && checked -> disabledActiveContentColor
            else -> disabledInactiveContentColor
        }
    }

    /**
     * Represents the container color passed to the items
     *
     * @param enabled whether the [SegmentedButton] is enabled or not
     * @param active whether the [SegmentedButton] item is active or not
     */
    @Stable
    internal fun containerColor(enabled: Boolean, active: Boolean): Color {
        return when {
            enabled && active -> activeContainerColor
            enabled && !active -> inactiveContainerColor
            !enabled && active -> disabledActiveContainerColor
            else -> disabledInactiveContainerColor
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other === null) return false
        if (this::class != other::class) return false

        other as SegmentedButtonColors

        if (activeBorderColor != other.activeBorderColor) return false
        if (activeContentColor != other.activeContentColor) return false
        if (activeContainerColor != other.activeContainerColor) return false
        if (inactiveBorderColor != other.inactiveBorderColor) return false
        if (inactiveContentColor != other.inactiveContentColor) return false
        if (inactiveContainerColor != other.inactiveContainerColor) return false
        if (disabledActiveBorderColor != other.disabledActiveBorderColor) return false
        if (disabledActiveContentColor != other.disabledActiveContentColor) return false
        if (disabledActiveContainerColor != other.disabledActiveContainerColor) return false
        if (disabledInactiveBorderColor != other.disabledInactiveBorderColor) return false
        if (disabledInactiveContentColor != other.disabledInactiveContentColor) return false
        if (disabledInactiveContainerColor != other.disabledInactiveContainerColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = activeBorderColor.hashCode()
        result = 31 * result + activeContentColor.hashCode()
        result = 31 * result + activeContainerColor.hashCode()
        result = 31 * result + inactiveBorderColor.hashCode()
        result = 31 * result + inactiveContentColor.hashCode()
        result = 31 * result + inactiveContainerColor.hashCode()
        result = 31 * result + disabledActiveBorderColor.hashCode()
        result = 31 * result + disabledActiveContentColor.hashCode()
        result = 31 * result + disabledActiveContainerColor.hashCode()
        result = 31 * result + disabledInactiveBorderColor.hashCode()
        result = 31 * result + disabledInactiveContentColor.hashCode()
        result = 31 * result + disabledInactiveContainerColor.hashCode()
        return result
    }
}

/**
 * Iterates through a [List] using the index and calls [action] for each item.
 * This does not allocate an iterator like [Iterable.forEach].
 *
 * **Do not use for collections that come from public APIs**, since they may not support random
 * access in an efficient way, and this method may actually be a lot slower. Only use for
 * collections that are created by code we control and are known to support random access.
 */
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    contract { callsInPlace(action) }
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

/**
 * Returns a list containing the results of applying the given [transform] function
 * to each element in the original collection.
 *
 * **Do not use for collections that come from public APIs**, since they may not support random
 * access in an efficient way, and this method may actually be a lot slower. Only use for
 * collections that are created by code we control and are known to support random access.
 */
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
inline fun <T, R> List<T>.fastMap(transform: (T) -> R): List<R> {
    contract { callsInPlace(transform) }
    val target = ArrayList<R>(size)
    fastForEach {
        target += transform(it)
    }
    return target
}

/**
 * Returns the first element yielding the largest value of the given function or `null` if there
 * are no elements.
 *
 * **Do not use for collections that come from public APIs**, since they may not support random
 * access in an efficient way, and this method may actually be a lot slower. Only use for
 * collections that are created by code we control and are known to support random access.
 */
@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
inline fun <T, R : Comparable<R>> List<T>.fastMaxBy(selector: (T) -> R): T? {
    contract { callsInPlace(selector) }
    if (isEmpty()) return null
    var maxElem = get(0)
    var maxValue = selector(maxElem)
    for (i in 1..lastIndex) {
        val e = get(i)
        val v = selector(e)
        if (maxValue < v) {
            maxElem = e
            maxValue = v
        }
    }
    return maxElem
}