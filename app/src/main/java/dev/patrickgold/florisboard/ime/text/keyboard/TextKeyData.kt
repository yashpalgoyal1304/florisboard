/*
 * Copyright (C) 2021 Patrick Goldinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.ime.text.keyboard

import dev.patrickgold.florisboard.ime.keyboard.KeyData
import dev.patrickgold.florisboard.ime.popup.PopupSet
import dev.patrickgold.florisboard.ime.text.key.KeyCode
import dev.patrickgold.florisboard.ime.text.key.KeyType
import kotlinx.serialization.*
import java.util.*

interface TextKeyData : KeyData {
    val type: KeyType
    val code: Int
    val label: String
    val groupId: Int

    override fun asString(isForDisplay: Boolean): String {
        return StringBuilder().run {
            if (!isForDisplay && code == KeyCode.MULTIPLE_CODE_POINTS) {
                if (this@TextKeyData is MultiTextKeyData) {
                    for (codePoint in codePoints) {
                        if (Character.isBmpCodePoint(codePoint)) {
                            append(codePoint.toChar())
                        } else {
                            try {
                                append(Character.toChars(codePoint))
                            } catch (_: Throwable) {
                            }
                        }
                    }
                }
            } else if (isForDisplay || code == KeyCode.URI_COMPONENT_TLD || code < KeyCode.SPACE) {
                // Combining Diacritical Marks
                // See: https://en.wikipedia.org/wiki/Combining_Diacritical_Marks
                if (code in 0x0300..0x036F) {
                    append("◌")
                }
                append(label)
            } else {
                if (Character.isBmpCodePoint(code)) {
                    append(code.toChar())
                } else {
                    try {
                        append(Character.toChars(code))
                    } catch (_: Throwable) {
                    }
                }
            }
            toString()
        }
    }

    companion object {
        /** Predefined key data for [KeyCode.ARROW_DOWN] */
        val ARROW_DOWN = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.ARROW_DOWN,
            label = "arrow_down"
        )

        /** Predefined key data for [KeyCode.ARROW_LEFT] */
        val ARROW_LEFT = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.ARROW_LEFT,
            label = "arrow_left"
        )

        /** Predefined key data for [KeyCode.ARROW_RIGHT] */
        val ARROW_RIGHT = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.ARROW_RIGHT,
            label = "arrow_right"
        )

        /** Predefined key data for [KeyCode.ARROW_UP] */
        val ARROW_UP = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.ARROW_UP,
            label = "arrow_up"
        )

        /** Predefined key data for [KeyCode.CLIPBOARD_COPY] */
        val CLIPBOARD_COPY = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.CLIPBOARD_COPY,
            label = "clipboard_copy"
        )

        /** Predefined key data for [KeyCode.CLIPBOARD_CUT] */
        val CLIPBOARD_CUT = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.CLIPBOARD_CUT,
            label = "clipboard_cut"
        )

        /** Predefined key data for [KeyCode.CLIPBOARD_PASTE] */
        val CLIPBOARD_PASTE = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.CLIPBOARD_PASTE,
            label = "clipboard_paste"
        )

        /** Predefined key data for [KeyCode.CLIPBOARD_SELECT] */
        val CLIPBOARD_SELECT = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.CLIPBOARD_SELECT,
            label = "clipboard_select"
        )

        /** Predefined key data for [KeyCode.CLIPBOARD_SELECT_ALL] */
        val CLIPBOARD_SELECT_ALL = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.CLIPBOARD_SELECT_ALL,
            label = "clipboard_select_all"
        )

        /** Predefined key data for [KeyCode.DELETE] */
        val DELETE = BasicTextKeyData(
            type = KeyType.ENTER_EDITING,
            code = KeyCode.DELETE,
            label = "delete"
        )

        /** Predefined key data for [KeyCode.DELETE_WORD] */
        val DELETE_WORD = BasicTextKeyData(
            type = KeyType.ENTER_EDITING,
            code = KeyCode.DELETE_WORD,
            label = "delete_word"
        )

        /** Predefined key data for [KeyCode.INTERNAL_BATCH_EDIT] */
        val INTERNAL_BATCH_EDIT = BasicTextKeyData(
            type = KeyType.FUNCTION,
            code = KeyCode.INTERNAL_BATCH_EDIT,
            label = "internal_batch_edit"
        )

        /** Predefined key data for [KeyCode.MOVE_START_OF_LINE] */
        val MOVE_START_OF_LINE = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.MOVE_START_OF_LINE,
            label = "move_start_of_line"
        )

        /** Predefined key data for [KeyCode.MOVE_END_OF_LINE] */
        val MOVE_END_OF_LINE = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.MOVE_END_OF_LINE,
            label = "move_end_of_line"
        )

        /** Predefined key data for [KeyCode.MOVE_START_OF_PAGE] */
        val MOVE_START_OF_PAGE = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.MOVE_START_OF_PAGE,
            label = "move_start_of_page"
        )

        /** Predefined key data for [KeyCode.MOVE_END_OF_PAGE] */
        val MOVE_END_OF_PAGE = BasicTextKeyData(
            type = KeyType.NAVIGATION,
            code = KeyCode.MOVE_END_OF_PAGE,
            label = "move_end_of_page"
        )

        /** Predefined key data for [KeyCode.REDO] */
        val REDO = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.REDO,
            label = "redo"
        )

        /** Predefined key data for [KeyCode.SHOW_INPUT_METHOD_PICKER] */
        val SHOW_INPUT_METHOD_PICKER = BasicTextKeyData(
            type = KeyType.FUNCTION,
            code = KeyCode.SHOW_INPUT_METHOD_PICKER,
            label = "show_input_method_picker"
        )

        /** Predefined key data for [KeyCode.SWITCH_TO_TEXT_CONTEXT] */
        val SWITCH_TO_TEXT_CONTEXT = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.SWITCH_TO_TEXT_CONTEXT,
            label = "switch_to_text_context"
        )
        /** Predefined key data for [KeyCode.SWITCH_TO_CLIPBOARD_CONTEXT] */
        val SWITCH_TO_CLIPBOARD_CONTEXT = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.SWITCH_TO_CLIPBOARD_CONTEXT,
            label = "switch_to_clipboard_context"
        )

        /** Predefined key data for [KeyCode.SHIFT] */
        val SHIFT = BasicTextKeyData(
            type = KeyType.MODIFIER,
            code = KeyCode.SHIFT,
            label = "shift"
        )

        /** Predefined key data for [KeyCode.SHIFT_LOCK] */
        val SHIFT_LOCK = BasicTextKeyData(
            type = KeyType.MODIFIER,
            code = KeyCode.SHIFT_LOCK,
            label = "shift_lock"
        )

        /** Predefined key data for [KeyCode.SPACE] */
        val SPACE = BasicTextKeyData(
            type = KeyType.CHARACTER,
            code = KeyCode.SPACE,
            label = "space"
        )

        /** Predefined key data for [KeyCode.UNDO] */
        val UNDO = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.UNDO,
            label = "undo"
        )

        /** Predefined key data for [KeyCode.UNSPECIFIED] */
        val UNSPECIFIED = BasicTextKeyData(
            type = KeyType.UNSPECIFIED,
            code = KeyCode.UNSPECIFIED,
            label = "unspecified"
        )

        /** Predefined key data for [KeyCode.VIEW_CHARACTERS] */
        val VIEW_CHARACTERS = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.VIEW_CHARACTERS,
            label = "view_characters"
        )

        /** Predefined key data for [KeyCode.VIEW_SYMBOLS] */
        val VIEW_SYMBOLS = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.VIEW_SYMBOLS,
            label = "view_symbols"
        )

        /** Predefined key data for [KeyCode.VIEW_SYMBOLS2] */
        val VIEW_SYMBOLS2 = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.VIEW_SYMBOLS2,
            label = "view_symbols2"
        )

        /** Predefined key data for [KeyCode.VIEW_NUMERIC_ADVANCED] */
        val VIEW_NUMERIC_ADVANCED = BasicTextKeyData(
            type = KeyType.SYSTEM_GUI,
            code = KeyCode.VIEW_NUMERIC_ADVANCED,
            label = "view_numeric_advanced"
        )

        /**
         * Constant for the default group. If not otherwise specified, any key is automatically
         * assigned to this group.
         */
        const val GROUP_DEFAULT: Int = 0

        /**
         * Constant for the Left modifier key group. Any key belonging to this group will get the
         * popups specified for "~left" in the popup mapping.
         */
        const val GROUP_LEFT: Int = 1

        /**
         * Constant for the right modifier key group. Any key belonging to this group will get the
         * popups specified for "~right" in the popup mapping.
         */
        const val GROUP_RIGHT: Int = 2

        /**
         * Constant for the enter modifier key group. Any key belonging to this group will get the
         * popups specified for "~enter" in the popup mapping.
         */
        const val GROUP_ENTER: Int = 3
    }
}

/**
 * Data class which describes a single key and its attributes.
 *
 * @property type The type of the key. Some actions require both [code] and [type] to match in order
 *  to be successfully executed. Defaults to [KeyType.CHARACTER].
 * @property code The UTF-8 encoded code of the character. The code defined here is used as the
 *  data passed to the system. Defaults to 0.
 * @property label The string used to display the key in the UI. Is not used for the actual data
 *  passed to the system. Should normally be the exact same as the [code]. Defaults to an empty
 *  string.
 */
@Serializable
@SerialName("text_key")
class BasicTextKeyData(
    override val type: KeyType = KeyType.CHARACTER,
    override val code: Int = KeyCode.UNSPECIFIED,
    override val label: String = "",
    override val groupId: Int = TextKeyData.GROUP_DEFAULT,
    val popup: PopupSet<TextKeyData>? = null
) : TextKeyData {
    override fun computeTextKeyData(evaluator: TextComputingEvaluator): TextKeyData? {
        return if (evaluator.isSlot(this)) {
            val slotData = evaluator.getSlotData(this)
            if (slotData != null) {
                BasicTextKeyData(slotData.type, slotData.code, slotData.label, slotData.groupId, popup)
            } else {
                null
            }
        } else {
            this
        }
    }

    override fun toString(): String {
        return "${BasicTextKeyData::class.simpleName} { type=$type code=$code label=\"$label\" groupId=$groupId }"
    }
}

@Serializable
@SerialName("auto_text_key")
class AutoTextKeyData(
    override val type: KeyType = KeyType.CHARACTER,
    override val code: Int = KeyCode.UNSPECIFIED,
    override val label: String = "",
    override val groupId: Int = TextKeyData.GROUP_DEFAULT,
    val popup: PopupSet<TextKeyData>? = null
) : TextKeyData {
    @Transient private val lower: BasicTextKeyData =
        BasicTextKeyData(type, Character.toLowerCase(code), label.lowercase(Locale.getDefault()), groupId, popup)
    @Transient private val upper: BasicTextKeyData =
        BasicTextKeyData(type, Character.toUpperCase(code), label.uppercase(Locale.getDefault()), groupId, popup)

    override fun computeTextKeyData(evaluator: TextComputingEvaluator): TextKeyData? {
        return if (evaluator.isSlot(this)) {
            val slotData = evaluator.getSlotData(this)
            if (slotData != null) {
                BasicTextKeyData(slotData.type, slotData.code, slotData.label, slotData.groupId, popup)
            } else {
                null
            }
        } else {
            if (evaluator.evaluateCaps(this)) { upper } else { lower }
        }
    }

    override fun toString(): String {
        return "${AutoTextKeyData::class.simpleName} { type=$type code=$code label=\"$label\" groupId=$groupId }"
    }
}

@Serializable
@SerialName("multi_text_key")
class MultiTextKeyData(
    override val type: KeyType = KeyType.CHARACTER,
    val codePoints: IntArray = intArrayOf(),
    override val label: String = "",
    override val groupId: Int = TextKeyData.GROUP_DEFAULT,
    val popup: PopupSet<TextKeyData>? = null
) : TextKeyData {
    @Transient override val code: Int = KeyCode.MULTIPLE_CODE_POINTS

    override fun computeTextKeyData(evaluator: TextComputingEvaluator): TextKeyData {
        return this
    }

    override fun toString(): String {
        return "${AutoTextKeyData::class.simpleName} { type=$type code=$code label=\"$label\" groupId=$groupId }"
    }
}
