/**
 * Excludes properties from serialization output.
 *
 * Supports:
 * <ul>
 *     <li>DenyAll</li>
 *     <li>RolesAllowed</li>
 * </ul>
 *
 * For example:
 * <pre>
 * public class User {
 *
 *     private String nickname;
 *
 *     private String username;
 *
 *     private String password;
 *
 *     public String getNickname() {
 *         return nickname;
 *     }
 *
 *     &#064;RolesAllowed("ROLE_ADMIN")
 *     public String getUsername() {
 *         return username;
 *     }
 *
 *     &#064;DenyAll
 *     public String getPassword() {
 *         return password;
 *     }
 *
 * }
 * </pre>
 * The {@code username} will be excluded if currently authenticated principal
 * is not in ADMIN role, and the {@code password} will be always excluded.
 */
package org.oxerr.jackson.module.jsr250;
