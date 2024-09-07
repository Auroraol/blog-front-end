export function isEmpty(value: any): value is false | 0 | 0n | "" | null | undefined | number {
    return (
        value === false ||
        value === 0 ||
        value === 0n ||
        value === "" ||
        value === null ||
        value === undefined ||
        (typeof value === "number" && Number.isNaN(value)) ||
        (Array.isArray(value) && value.length === 0) ||
        (typeof value === "object" && value !== null && Object.keys(value).length === 0)
    );
}

export function isNotEmpty(value: any): value is true | number | string | object {
    return !isEmpty(value);
}