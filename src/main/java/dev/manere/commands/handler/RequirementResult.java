package dev.manere.commands.handler;

import org.jetbrains.annotations.NotNull;

public class RequirementResult {
    public static final RequirementResult PASSED = RequirementResult.result(-1);
    public static final RequirementResult FAILED = RequirementResult.result(0);

    private final int result;

    private RequirementResult(final int result) {
        this.result = result;
    }

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

    public int result() {
        return result;
    }

    @NotNull
    public static RequirementResult passed() {
        return PASSED;
    }

    @NotNull
    public static RequirementResult failed() {
        return FAILED;
    }
}
