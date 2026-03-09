import {View, Text, Pressable} from 'react-native'
import React, {ReactNode} from 'react'
import {twMerge} from "tailwind-merge";

type ButtonVariant = 'primary' | 'secondary' | 'neutral';

interface ButtonProps {
    title: string;
    variant?: 'primary' | 'secondary';
    disabled?: boolean;
    iconLeft?: ReactNode;
    iconRight?: ReactNode;
    onPress: () => void;
}



const Button = (props: ButtonProps) => {
    const {
        title = 'Button',
        variant = 'primary',
        disabled = false,
        iconLeft,
        iconRight,
        onPress,
    } = props;




    return (
        <Pressable
            onPress={onPress}
            disabled={disabled}
            className={twMerge(`flex items-center ${iconLeft ? "justify-between  flex-row-reversed" : 'flex-row' + iconRight ? "justify-between" : 'justify-center'}`, )}
        >

        </Pressable>
    )
}

export default Button
