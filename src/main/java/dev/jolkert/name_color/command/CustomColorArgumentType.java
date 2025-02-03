package dev.jolkert.name_color.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CustomColorArgumentType implements ArgumentType<Integer>
{
	private static final Collection<String> EXAMPLES = Arrays.asList("red", "#FF0000");
	public static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType(
			color -> Text.stringifiedTranslatable("argument.color.invalid", color)
	);

	private CustomColorArgumentType()
	{
	}

	public static CustomColorArgumentType color()
	{
		return new CustomColorArgumentType();
	}

	public static int getColor(CommandContext<ServerCommandSource> context, String name)
	{
		return context.getArgument(name, Integer.class);
	}

	public Integer parse(StringReader stringReader) throws CommandSyntaxException
	{
		String string = stringReader.readUnquotedString();
		Formatting formatting = Formatting.byName(string);

		if (formatting != null && !formatting.isModifier())
		{
			return formatting.getColorValue();
		}
		else
		{
			if (string.startsWith("#") && string.length() == 7)
			{
				try
				{
					return Integer.parseInt(string.substring(1), 16);
				}
				catch (NumberFormatException ignored)
				{
				}
			}
			throw INVALID_COLOR_EXCEPTION.createWithContext(stringReader, string);
		}
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return CommandSource.suggestMatching(Formatting.getNames(true, false), builder);
	}

	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
}

