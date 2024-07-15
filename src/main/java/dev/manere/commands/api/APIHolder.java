package dev.manere.commands.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds the singleton instance of the CommandsAPI.
 */
@ApiStatus.Internal
public class APIHolder {
    /**
     * The singleton instance of the CommandsAPI.
     */
    @Nullable
    public static CommandsAPI API = null;

    /**
     * Retrieves the current CommandsAPI instance.
     *
     * @return the CommandsAPI instance.
     * @throws NullPointerException if the API has not been initialized.
     */
    @NotNull
    public static CommandsAPI api() {
        if (API == null) throw new NullPointerException();
        return API;
    }

    /**
     * Initializes the CommandsAPI instance.
     *
     * @param api the CommandsAPI instance to initialize.
     */
    public static void init(final @NotNull CommandsAPI api) {
        API = api;
    }
}