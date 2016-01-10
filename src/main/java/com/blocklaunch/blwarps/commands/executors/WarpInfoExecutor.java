package com.blocklaunch.blwarps.commands.executors;

import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class WarpInfoExecutor implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        Optional<Warp> optWarp = args.getOne("warp");
        if (!optWarp.isPresent()) {
            source.sendMessage(Constants.WARP_NOT_FOUND_MSG);
            return CommandResult.empty();
        }

        Warp warp = optWarp.get();

        if (Util.hasPermission(source, warp) == false) {
            source.sendMessage(Constants.NO_PERMISSION_MSG);
            return CommandResult.empty();
        }

        Text warpName = Util.warpText(warp);

        List<Text> warpInfo = Lists.newArrayList();

        warpInfo.add(Text.of(TextColors.BLUE, "---------------", warpName, "---------------"));
        warpInfo.add(Text.of(TextColors.BLUE, "Name: ", warpName));
        warpInfo.add(Text.of(TextColors.BLUE, "World: ", TextColors.WHITE, warp.getWorld()));
        warpInfo.add(Text.of(TextColors.BLUE, "Location: ", TextColors.WHITE, warp.getPosition()));
        warpInfo.add(Text.of(TextColors.BLUE, "Groups: ", generateGroupList(warp)));

        for (Text infoLine : warpInfo) {
            source.sendMessage(infoLine);
        }

        return CommandResult.success();

    }

    private Text generateGroupList(Warp warp) {
        List<String> groups = warp.getGroups();
        if (groups.isEmpty()) {
            return Text.of("none");
        }

        Builder builder = Text.builder();
        for (int index = 0; index < groups.size(); index++) {
            builder.append(Util.warpGroupInfoText(groups.get(index)));
            if (groups.size() - 1 != index) {
                // Not the last group name in the list
                builder.append(Text.of(", "));
            }
        }

        return builder.build();
    }

}
