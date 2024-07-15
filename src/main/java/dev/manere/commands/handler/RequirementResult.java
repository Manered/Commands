package dev.manere.commands.handler;

import org.jetbrains.annotations.NotNull;

/**
 * Represents the result of a command requirement check.
 */
public class RequirementResult {
    public static final RequirementResult PASSED = RequirementResult.result(-1);
    public static final RequirementResult FAILED = RequirementResult.result(0);

    private final int result;

    private RequirementResult(final int result) {
        this.result = result;
    }

    /**
     * Creates a new RequirementResult with the specified result value.
     *
     * @param result the result value.
     * @return a new RequirementResult instance.
     */
    @NotNull
    private static RequirementResult result(final int result) {
        return new RequirementResult(result);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof RequirementResult otherResult && otherResult.result() == result();
    }

    @Override
    public String toString() {
        return "RequirementResult[result=" + result + "]";
    }

    /**
     * Gets the result value.
     *
     * @return the result value.
     */
    public int result() {
        return result;
    }

    /**
     * Gets a RequirementResult indicating that the requirement check passed.
     *
     * @return a RequirementResult indicating that the requirement check passed.
     */
    @NotNull
    public static RequirementResult passed() {
        return PASSED;
    }

    /**
     * Gets a RequirementResult indicating that the requirement check failed.
     *
     * @return a RequirementResult indicating that the requirement check failed.
     */
    @NotNull
    public static RequirementResult failed() {
        return FAILED;
    }
}