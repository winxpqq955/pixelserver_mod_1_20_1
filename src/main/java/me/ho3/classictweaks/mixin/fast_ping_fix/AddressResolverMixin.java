package me.ho3.classictweaks.mixin.fast_ping_fix;

import me.fallenbreath.fastipping.impl.InetAddressPatcher;
import net.minecraft.client.network.Address;
import net.minecraft.client.network.AddressResolver;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Mixin(value = AddressResolver.class, priority = 200)
@SuppressWarnings("all")
public interface AddressResolverMixin
{
	/**
	 * @author Fallen_Breath
	 * @reason Forge does not support interface mixin. Here's a vanilla copy one, with manual "@ModifyVariable"
	 */
	@SuppressWarnings("all")
	@Overwrite
	static Optional<Address> method_36903(ServerAddress address)
	{
		try
		{
			InetAddress inetAddress = InetAddress.getByName(address.getAddress());
			// @ModifyVariable
			inetAddress = InetAddressPatcher.patch(address.getAddress(), inetAddress);
			return Optional.of(Address.create(new InetSocketAddress(inetAddress, address.getPort())));
		}
		catch (UnknownHostException var2)
		{
			AddressResolver.LOGGER.debug("Couldn't resolve server {} address", address.getAddress(), var2);
			return Optional.empty();
		}
	}
}