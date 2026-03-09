// button.variants.ts
import { cva } from "class-variance-authority";

export const buttonVariants = cva(
  "px-6 py-4 rounded-xl",
  {
    variants: {
      variant: {
        primary: "border-none bg-green-600 text-gray-50",
        secondary: "bg-gray-200 text-gra",
        neutral: "bg-white border border-gray-300",
      },

      disabled: {
        true: "opacity-50",
        false: "",
      },

      iconPosition: {
        none: "",
        left: "flex-row",
        right: "flex-row-reverse",
      }
    },

    defaultVariants: {
      variant: "primary",
      disabled: false,
      iconPosition: "none",
    }
  }
);